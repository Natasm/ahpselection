package ahp;

import java.util.ArrayList;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;

public class AHP {
	
	/**
     * Random Consistency Index
     */
    protected static double RI[] = {0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49};

    /**
     * The matrix
     */
    protected Array2DRowRealMatrix mtx;

    /**
     * Contains
     */
    protected double pairwiseComparisonArray[];

    /**
     * Number of alternatives
     */
    protected int nrAlternatives;

    /**
     * The resulting weights/priorities
     */
    protected ArrayList<Double> weights;

    /**
     * Corresponds to the weights
     */
    protected String labels[] = null;

    /**
     *
     */
    protected EigenDecomposition evd;

    /**
     * Convenience array, i.e. comparisonIndices[length=NumberOfPairwiseComparisons][2] 
     * Contains minimum number of comparisons. 
     */
    protected int[][] comparisonIndices;

    /**
     * Index of the greatest Eigenvalue/ -vector
     */
    protected int evIdx = 0; // index of actual eigenvalue/-vector

    /**
     *
     * @param labels
     */
    public AHP(String labels[]) {
        this(labels.length);
        this.labels = labels;
    }

    /**
     * Construct an AHP with number of alternatives
     * @param nrAlternatives
     */
    public AHP(int nrAlternatives) {
        this.nrAlternatives = nrAlternatives;
        mtx = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);
        weights = new ArrayList<Double>();

        pairwiseComparisonArray = new double[getNrOfPairwiseComparisons()];
        comparisonIndices = new int[getNrOfPairwiseComparisons()][];
        for (int i = 0; i < getNrOfPairwiseComparisons(); i++) {
            comparisonIndices[i] = new int[2];
        }

        // only need diagonal 1, but set everything to 1.0
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = 0; col < nrAlternatives; col++) {
                mtx.setEntry(row, col, 1.0);
            }
        }
    }

    /**
     *
     * @return the number of pairwise comparisons which have to be done by the user
     */
    public int getNrOfPairwiseComparisons() {
        return ((nrAlternatives - 1) * nrAlternatives) / 2;
    }

    /**
     *
     * @return the user input of the pairwise comparisons
     */
    public double[] getPairwiseComparisonArray() {
        return pairwiseComparisonArray;
    }

    /**
     * Set the pairwise comparison scores and calculate all relevant numbers
     * @param a 
     */
    public void setPairwiseComparisonArray(double a[]) {
        int i = 0;
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = row + 1; col < nrAlternatives; col++) {
                //System.out.println(row + "/" + col + "=" + a[i]);
                mtx.setEntry(row, col, a[i]);
                mtx.setEntry(col, row, 1.0 / mtx.getEntry(row, col));
                comparisonIndices[i][0] = row;
                comparisonIndices[i][1] = col;
                i++;
            }
        }
       
        GeometricMean geometricMean = new GeometricMean();
        ArrayList<Double> weightsAux = new ArrayList<>();
        for(int row = 0; row < mtx.getRowDimension(); row++) {
        	geometricMean.clear();
        	weightsAux.add(geometricMean.evaluate(mtx.getRow(row)));
        }
		
        for (double d: weightsAux){
        	this.weights.add(d / sum(weightsAux));
        }
        
        /*System.out.println(mtx);
        System.out.println("Entrei");
        evd = new EigenDecomposition(mtx);
        System.out.println("Passei");

        evIdx = 0;
        for (int k = 0; k < evd.getRealEigenvalues().length; k++) {
            //System.out.println(evd.getRealEigenvalues()[k]);
            evIdx = (evd.getRealEigenvalue(k) > evd.getRealEigenvalue(evIdx)) ? k : evIdx;
        }
        //System.out.println("evIdx=" + evIdx);
        //System.out.println("EigenValue=" + evd.getRealEigenvalue(evIdx));

        double sum = 0.0;
        RealVector v = evd.getEigenvector(evIdx);
        for (double d : v.toArray()) {
            sum += d;
        }
        //System.out.println(sum);
        for (int k = 0; k < v.getDimension(); k++) {
            weights[k] = v.getEntry(k) / sum;
        }*/
    }
    
    private double sum(ArrayList<Double> doubleArray) {
    	double sum = 0;
    	for(double d: doubleArray) sum = sum + d;
    	return sum;
    }

    /**
     *
     * @param arrayIdx
     * @return
     */
    public int[] getIndicesForPairwiseComparison(int arrayIdx) {
        return comparisonIndices[arrayIdx];
    }

    /**
     *
     * @return resulting weights for alternatives
     */
    public ArrayList<Double> getWeights() {
        return weights;
    }

    /**
     *
     * @return the consistency index
     */
    public double getConsistencyIndex() {
        return (evd.getRealEigenvalue(evIdx) - (double) nrAlternatives) / (double) (nrAlternatives - 1);
    }

    /**
     *
     * @return the consistency ratio. Should be less than 10%
     */
    public double getConsistencyRatio() {
    	System.out.println("Tamanho em RI: " + RI.length);
        return getConsistencyIndex() / RI[nrAlternatives % RI.length] * 100.0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<nrAlternatives; i++)
            sb.append(mtx.getRowVector(i) + "\n");
        return sb.toString();
    }
    
    public AHP Calculate(AHP ahp, String[] labels) {
            
        // System.out.println("\n" + ahp + "\n");
        
        /*for (int i = 0; i < ahp.getNrOfPairwiseComparisons(); i++) {
            System.out.print("\nImportance of " + labels[ahp.getIndicesForPairwiseComparison(i)[0]] + " compared to ");
            System.out.print(labels[ahp.getIndicesForPairwiseComparison(i)[1]] + "= ");
            System.out.println(ahp.getPairwiseComparisonArray()[i]);
        }

        System.out.println("Consistency Index: " + ahp.getConsistencyIndex());
        System.out.println("Consistency Ratio: " + ahp.getConsistencyRatio() + "%");
        System.out.println();
        System.out.println("Weights: ");
        for (int k=0; k<ahp.getWeights().length; k++) {
            System.out.println(labels[k] + ": " + ahp.getWeights()[k] * 100);
        }*/
        return ahp;
    }
}
