package multiValuedNominalAlpha.mvnaCalculator;

import java.util.*;
import mathCollection.HashMathSet;
import mathCollection.MathSet;
import mathCollection.SetOfSets;
import multiValuedNominalAlpha.mvnaCalculator.model.LabelledFloatMatrix;
import multiValuedNominalAlpha.mvnaCalculator.model.Labels;
import multiValuedNominalAlpha.mvnaCalculator.model.ReliabilityDataMatrix;

public class MultiValuedAlphaCalculator {


    private ReliabilityDataMatrix dataMatrix;
    private boolean verbose;
    private StringBuffer calculationsString;
    private StringBuffer valuesLog;
    private int[] multiValuesPerUnit;
    private int numberOfMultiValues;
    private MathSet allMultiValues;
    private MathSet allValues;
    private HashMap valueCount;
    private HashMap numberOfSizedMultiLabels;
    private HashMap proportionOfSizedMultiLabels;
    private int totalValuesUsed;
    private LabelledFloatMatrix coincidenceMatrix;
    private LabelledFloatMatrix observedDeltaMatrix;
    private LabelledFloatMatrix ckFreqProductMatrix;
    private HashMap<Integer, HashMap<Integer, Double>> sizeProductMatrix;
    private LabelledFloatMatrix expectedDeltaMatrix;
    private int numberOfMissingCells = 0;
    private int numberOfEmptyCells = 0;
    private double alpha;
    private double Do;
    private double De;
    private boolean isDoCalculated = false;
    private final static String newline = System.getProperty("line.separator");

    public MultiValuedAlphaCalculator(ReliabilityDataMatrix rdm, boolean logCalculations) {
        this.dataMatrix = rdm;
        this.verbose = logCalculations;
    }

    public void calculateDo() {
        this.calculationsString = new StringBuffer();
        this.valuesLog = new StringBuffer();

        this.valuesLog.append("- Reliability data statistics -").append(newline).append(newline);

        if (this.verbose) {
            this.calculationsString.append("- Calculations log -" + newline);
        } else {
            this.calculationsString.append("Calculations not logged" + newline + newline);
            this.calculationsString.append("To view calculations, Select Log Calculationson the Reliability Data Tab" + newline);
        }

        getPreliminaryValues();
        constructObservedDeltaMatrix();
        constructCoincidenceMatrix();

        this.valuesLog.append("Percentage agreement = " + (1.0D - this.Do) * 100.0D + newline);

        this.isDoCalculated = true;
    }

    public void addResultsText(String s) {
        this.valuesLog.append(s).append(newline);
    }

    public void calculateAlpha() {
        if (!this.isDoCalculated) {
            calculateDo();
        }
        this.Do = getDo();

        constructAllProductMatrix();
        constructExpectedDeltaMatrix();
        calculateDe();

        this.calculationsString.append("Alpha = 1 - " + this.Do + "/" + this.De);
        this.alpha = (1.0D - this.Do / this.De);

        if (this.De == 0 && this.Do == 0) {
            this.alpha = 0;
        }
    }

    private void constructCoincidenceMatrix() {
        if (this.verbose) {
            this.calculationsString.append(newline + "- Calculating Observed Disagreement -" + newline);
        }

        this.coincidenceMatrix = new LabelledFloatMatrix();

        double totalDisagreement = 0.0D;

        for (int unit = 0; unit < this.dataMatrix.getNumberOfUnits(); unit++) {
            for (int coderC = 0; coderC < this.dataMatrix.getNumberOfCoders(); coderC++) {
                for (int coderK = coderC + 1; coderK < this.dataMatrix.getNumberOfCoders();
                     coderK++) {
                    Labels multiValueC = this.dataMatrix.getLabels(unit, coderC);
                    Labels multiValueK = this.dataMatrix.getLabels(unit, coderK);

                    if ((multiValueC != null) && (multiValueK != null)) {
                        double multiValueDeltack = this.observedDeltaMatrix.get(multiValueC, multiValueK);

                        double contributionCK = 1.0D / (this.multiValuesPerUnit[unit] - 1);

                        this.coincidenceMatrix.add(multiValueC, multiValueK, contributionCK);

                        this.coincidenceMatrix.add(multiValueK, multiValueC, contributionCK);

                        totalDisagreement += contributionCK * multiValueDeltack;

                        if (this.verbose) {
                            this.calculationsString.append("Adding disagreement contributed by coders " + coderC + " and " + coderK + " for unit " + unit + newline);

                            this.calculationsString.append("C = " + multiValueC + ", K =" + multiValueK + ", DeltaCK = " + multiValueDeltack + " 1/mu = " + contributionCK + newline);

                            this.calculationsString.append("Disagreement so far is " + totalDisagreement + newline);
                        }

                    }

                }

            }

        }

        this.Do = (totalDisagreement * 2.0D / this.numberOfMultiValues);

        if (this.verbose) {
            this.calculationsString.append("Observed disagreement = 2( measured_Disagreement / number_of_multiple_value)" + newline);

            this.calculationsString.append("Observed disagreement = 2( " + totalDisagreement + "/" + this.numberOfMultiValues + ") = " + this.Do + newline);
        }
    }

    private void constructObservedDeltaMatrix() {
        this.observedDeltaMatrix = new LabelledFloatMatrix();

        Iterator MVIterator1 = this.allMultiValues.iterator();

        while (MVIterator1.hasNext()) {
            Iterator MVIterator2 = this.allMultiValues.iterator();
            Labels nextMV1 = (Labels) MVIterator1.next();

            while (MVIterator2.hasNext()) {

                Labels nextMV2 = (Labels) MVIterator2.next();
                double deltaMV1MV2 = multiValueDelta(nextMV1, nextMV2);

                this.observedDeltaMatrix.put(nextMV1, nextMV2, deltaMV1MV2);
                this.observedDeltaMatrix.put(nextMV2, nextMV1, deltaMV1MV2);
            }
        }
    }

    private void getPreliminaryValues() {
        this.multiValuesPerUnit = new int[this.dataMatrix.getNumberOfUnits()];
        this.numberOfMultiValues = 0;
        this.allMultiValues = new HashMathSet();
        this.valueCount = new HashMap();
        MathSet allUsedSizes = new HashMathSet();
        this.numberOfSizedMultiLabels = new HashMap();

        boolean[] codedAtLeastOneUnit = new boolean[this.dataMatrix.getNumberOfCoders()];
        boolean[] codedByAtLeastOneCoder = new boolean[this.dataMatrix.getNumberOfUnits()];

        for (int unit = 0; unit < this.dataMatrix.getNumberOfUnits(); unit++) {
            for (int coder = 0; coder < this.dataMatrix.getNumberOfCoders(); coder++) {

                Labels currentMultiValue = this.dataMatrix.getLabels(unit, coder);

                if (currentMultiValue != null) {
                    codedAtLeastOneUnit[coder] = true;
                    codedByAtLeastOneCoder[unit] = true;

                    if (currentMultiValue.size() == 0)
                        this.numberOfEmptyCells += 1;
                } else {
                    this.numberOfMissingCells += 1;
                }
                if ((currentMultiValue != null) && (this.dataMatrix.getNumberOfLabelsForUnit(unit) > 1)) {
                    this.totalValuesUsed += currentMultiValue.size();
                    this.multiValuesPerUnit[unit] += 1;
                    this.numberOfMultiValues += 1;

                    this.allMultiValues.add(currentMultiValue);

                    Iterator currentValueIterator = currentMultiValue.iterator();

                    while (currentValueIterator.hasNext()) {
                        String nextValue = (String) currentValueIterator.next();

                        if (this.valueCount.containsKey(nextValue)) {
                            int countForValue = ((Integer) this.valueCount.get(nextValue)).intValue();

                            this.valueCount.remove(nextValue);
                            this.valueCount.put(nextValue, new Integer(countForValue + 1));
                        } else {
                            this.valueCount.put(nextValue, new Integer(1));
                        }

                    }

                    Integer sizeOfMultiValue = new Integer(currentMultiValue.size());

                    allUsedSizes.add(sizeOfMultiValue);

                    if (!this.numberOfSizedMultiLabels.containsKey(sizeOfMultiValue)) {
                        this.numberOfSizedMultiLabels.put(sizeOfMultiValue, new Integer(1));
                    } else {
                        int numberOfThisSize = ((Integer) this.numberOfSizedMultiLabels.get(sizeOfMultiValue)).intValue();

                        this.numberOfSizedMultiLabels.remove(sizeOfMultiValue);
                        this.numberOfSizedMultiLabels.put(sizeOfMultiValue, new Integer(numberOfThisSize + 1));
                    }
                }
            }
        }
        this.allValues = new HashMathSet(this.valueCount.keySet());
        int numberOfValuesUsed = this.allValues.size();

        int numberOfCodersThatCodedAtLeastOneUnit = 0;
        for (int i = 0; i < codedAtLeastOneUnit.length; i++) {
            if (codedAtLeastOneUnit[i] != false)
                numberOfCodersThatCodedAtLeastOneUnit += 1;
        }
        int numberOfUnitsCodedByAtLeastOneCoder = 0;
        for (int i = 0; i < codedByAtLeastOneCoder.length; i++) {
            if (codedByAtLeastOneCoder[i] != false) {
                numberOfUnitsCodedByAtLeastOneCoder += 1;
            }
        }

        this.proportionOfSizedMultiLabels = new HashMap();
        Iterator sizeIterator = this.numberOfSizedMultiLabels.keySet().iterator();
        while (sizeIterator.hasNext()) {
            Integer size = (Integer) sizeIterator.next();
            int number = ((Integer) this.numberOfSizedMultiLabels.get(size)).intValue();

            this.proportionOfSizedMultiLabels.put(size, new Float((float) number / (float) this.numberOfMultiValues));
        }

        this.valuesLog.append("Number of different individual values :" + numberOfValuesUsed + newline + newline);

        this.valuesLog.append("All individual values :" + orderAlphabetically(this.allValues) + newline + newline);
        this.valuesLog.append("Total number of individual values :" + this.totalValuesUsed + newline + newline);

        this.valuesLog.append("Number of uses of each value :" + getOrderedHashString(this.valueCount) + newline + newline);

        this.valuesLog.append("Number of (comparable) occupied cells :" + this.numberOfMultiValues + newline + newline);

        this.valuesLog.append("All multi values :" + orderAlphabetically(this.allMultiValues) + newline + newline);
        this.valuesLog.append("All used sizes of multi values :" + allUsedSizes + newline + newline);

        this.valuesLog.append("Number of multi values of each different size :" + getOrderedHashString(this.numberOfSizedMultiLabels) + newline + newline);

        this.valuesLog.append("Proportions multi values for each different size :" + getOrderedHashString(this.proportionOfSizedMultiLabels) + newline + newline);

        this.valuesLog.append("Number of coders that coded at least one unit :" + numberOfCodersThatCodedAtLeastOneUnit + newline + newline);

        this.valuesLog.append("Number of units coded by at least one coder :" + numberOfUnitsCodedByAtLeastOneCoder + newline + newline);

        this.valuesLog.append("Number of cells with missing data:" + this.numberOfMissingCells + newline + newline);

        this.valuesLog.append("Number of cells containing the empty set ({}):" + this.numberOfEmptyCells + newline + newline);
    }

    private void constructAllProductMatrix() {

        this.ckFreqProductMatrix = new LabelledFloatMatrix();

        Set allSizesSet = new TreeSet(this.numberOfSizedMultiLabels.keySet());
        this.sizeProductMatrix = new HashMap<>();

        while (!allSizesSet.isEmpty()) {    // Each size of set in turn

            int sizeC = ((Integer) allSizesSet.toArray()[0]).intValue(); // sizeC is the next smallest size
            Iterator sizeIterator = allSizesSet.iterator();

            while (sizeIterator.hasNext()) {            // Look over all sizes
                int sizeK = ((Integer) sizeIterator.next()).intValue(); // sizeK is each other size

                SetOfSets cPowerset = this.allValues.fixedSizeSubsets(sizeC); // all poss sets of size C
                SetOfSets kPowerset = this.allValues.fixedSizeSubsets(sizeK); // all poss sets of Size K

                Iterator cIterator = cPowerset.iterator();

                while (cIterator.hasNext()) {

                    Iterator kIterator = kPowerset.iterator();
                    Set cSet = (Set) cIterator.next();
                    Labels c = new Labels(cSet);

                    while (kIterator.hasNext()) {

                        Labels k = new Labels((Set) kIterator.next());

                        int ckProduct = getCombinedProductOfCAndK(c, k);
                        this.ckFreqProductMatrix.put(c, k, ckProduct);
                        this.ckFreqProductMatrix.put(k, c, ckProduct);

                        double d = getValueInMapOrZeroIfAbsent(sizeC, sizeK, this.sizeProductMatrix);

                        if ((!c.equals(k)) && (sizeC == sizeK)) {
                            d = d + ckProduct * 2;
                            putInIntIntMap(this.sizeProductMatrix, sizeC, sizeK, d);

                        } else if (sizeC != sizeK) {
                            putInIntIntMap(this.sizeProductMatrix, sizeC, sizeK, d + ckProduct);
                            putInIntIntMap(this.sizeProductMatrix, sizeK, sizeC, d + ckProduct);

                        } else {
                            putInIntIntMap(this.sizeProductMatrix, sizeC, sizeK, d + ckProduct);
                        }
                    }

                    kPowerset.remove(cSet);
                }
            }
            allSizesSet.remove(new Integer(sizeC));
        }
    }

    /**
     * Get a value from the map if it is there, otherwise return 0
     *
     * @param x          index into first map
     * @param y          index into second map
     * @param intintHash the map to look into
     * @return 0 or the number in the map.
     */
    private double getValueInMapOrZeroIfAbsent(int x, int y, HashMap<Integer, HashMap<Integer, Double>> intintHash) {

        double result = 0d;

        if (intintHash.containsKey(new Integer(x))) {
            if (intintHash.get(new Integer(x)).containsKey(new Integer(y))) {
                result = intintHash.get(new Integer(x)).get(new Integer(y));
            }
        }
        return result;
    }

    private int getCombinedProductOfCAndK(Labels c, Labels k) {

        MathSet mc = new HashMathSet(c.toSet());
        MathSet mk = new HashMathSet(k.toSet());

        int cProduct = productOfValueFrequencies(mc);
        int kDiffcProduct = productOfValueFrequencies(mk.difference(mc));
        int kIntersectionCProduct = productOfValueFrequencies_minusOne(mk.intersection(mc));

        return cProduct * kDiffcProduct * kIntersectionCProduct;
    }

    private void constructExpectedDeltaMatrix() {

        Set allPossMVs = this.ckFreqProductMatrix.getLabelsSet();

        this.expectedDeltaMatrix = new LabelledFloatMatrix();

        while (!allPossMVs.isEmpty()) {
            Labels c = (Labels) new ArrayList(allPossMVs).get(0);
            Iterator allPossMVs2 = allPossMVs.iterator();

            while (allPossMVs2.hasNext()) {
                Labels k = (Labels) allPossMVs2.next();

                double deltaCK = multiValueDelta(c, k);
                this.expectedDeltaMatrix.put(c, k, deltaCK);
                this.expectedDeltaMatrix.put(k, c, deltaCK);

                if (this.verbose) {
                    this.calculationsString.append(newline + "Added expected delta c=" + c + " k=" + k + " dck=" + deltaCK);
                }
            }

            allPossMVs.remove(c);
        }

//        this.expectedDeltaMatrix = new LabelledFloatMatrix();
//        Set<Labels> allPossMVs = this.ckFreqProductMatrix.getLabelsSet();
//
//        while (!allPossMVs.isEmpty()){
//            Labels c = (Labels) allPossMVs.toArray()[0]; // (MathSet)new ArrayList(allPossMVs).get(0);
//
//            for (Labels k : allPossMVs){
//
//                double deltaCK = multiValueDelta(c, k);
//                this.expectedDeltaMatrix.put(c, k, deltaCK);
//                this.expectedDeltaMatrix.put(k, c, deltaCK);
//
//                if (this.verbose) {
//                    this.calculationsString.append(newline + "Added expected delta c=" + c + " k=" + k + " dck=" + deltaCK);
//                }
//            }
//
//            allPossMVs.remove(c);
//        }
    }

    private void calculateDe() {

        if (this.verbose) {
            this.calculationsString.append(newline + "- Calculating Expected Disagreement -" + newline);
        }

        double totalDisagreement = 0.0D;
//
//        for (Labels c : this.ckFreqProductMatrix.getLabelsSet()) {
//
//            for (Labels k : this.ckFreqProductMatrix.getLabelsSet()) {

        Set allPossMVs = this.ckFreqProductMatrix.getLabelsSet();
        while (!allPossMVs.isEmpty()) {
            Labels c = (Labels) new ArrayList(allPossMVs).get(0);

            Iterator allPossMVs2 = allPossMVs.iterator();
            while (allPossMVs2.hasNext()) {
                Labels k = (Labels) allPossMVs2.next();

                double deltaCK = this.expectedDeltaMatrix.get(c, k);
                double propotionSizeC = ((Float) this.proportionOfSizedMultiLabels.get(new Integer(c.size()))).doubleValue();
                double propotionSizeK = ((Float) this.proportionOfSizedMultiLabels.get(new Integer(k.size()))).doubleValue();
                double sizeProduct = this.sizeProductMatrix.get(c.size()).get(k.size());
                double ckProduct = this.ckFreqProductMatrix.get(c, k);

                if (this.verbose) {
                    this.calculationsString.append("For " + c + " and " + k + " adding " + propotionSizeC + " * " + propotionSizeK + " * (" + ckProduct + "/" + sizeProduct + ") * " + deltaCK + newline);
                }

                if (sizeProduct > 0.0D) {
                    totalDisagreement += propotionSizeC * propotionSizeK * (ckProduct / sizeProduct) * deltaCK;
                }
            }
            allPossMVs.remove(c);
        }
        this.De = (totalDisagreement * 2.0D);
    }

    public double getDo() {
        return this.Do;
    }

    private int productOfValueFrequencies(MathSet c) {
        int product = 1;
        Iterator cIterator = c.iterator();
        while (cIterator.hasNext()) {
            product *= ((Integer) this.valueCount.get((String) cIterator.next())).intValue();
        }
        return product;
    }

    private int productOfValueFrequencies_minusOne(MathSet c) {
        int product = 1;
        Iterator cIterator = c.iterator();
        while (cIterator.hasNext()) {
            product *= (((Integer) this.valueCount.get((String) cIterator.next())).intValue() - 1);
        }
        return product;
    }

    private float multiValueDelta(Labels c, Labels k) {
        if (c.equals(k)) {
            return 0.0F;
        }

        MathSet mc = new HashMathSet(c.toSet());

        return 1.0F - 2.0F * mc.intersection(k.toSet()).size() / (c.size() + k.size());
    }


    public String getCalculations() {
        if (this.calculationsString.length() > 1000000) {
            return "Log to large to display, try File->Save Calculations";
        }
        return this.calculationsString.toString();
    }

    //TODO: this an the above are confusing
    public String getCalculationsText() {
        return this.calculationsString.toString();
    }

    public String getResultsText() {
        return this.valuesLog.toString();
    }

    public String getValues() {
        return this.valuesLog.toString();
    }

    private String getOrderedHashString(HashMap h) {
        String results = "{";

        ArrayList orderedKeys = orderAlphabetically(h.keySet());
        for (int i = 0; i < orderedKeys.size(); i++) {
            Object key = orderedKeys.get(i);
            String value = h.get(key).toString();

            if (results.length() > 1) {
                results = results + ", ";
            }
            results = results + key + "=" + value;
        }

        return results + "}";
    }

    private ArrayList orderAlphabetically(Set s) {
        Set c = new HashSet(s);

        ArrayList result = new ArrayList(c.size());
        while (!c.isEmpty()) {
            Object[] strings = c.toArray();
            String nextBest = strings[0].toString();
            Object chosenBest = strings[0];
            for (int i = 1; i < strings.length; i++) {
                String next = strings[i].toString();
                if (next.compareToIgnoreCase(nextBest) < 0) {
                    nextBest = next;
                    chosenBest = strings[i];
                }
            }

            result.add(chosenBest);
            c.remove(chosenBest);
        }

        return result;
    }

    public double getAlpha() {
        return this.alpha;
    }


    public LabelledFloatMatrix getCoincidenceMatrix() {
        return this.coincidenceMatrix;
    }

    public LabelledFloatMatrix getObservedDeltaMatrix() {
        return this.observedDeltaMatrix;
    }

    /**
     * Put a value into a int->int map. Create any sub maps if needed
     *
     * @param m the map
     * @param x the first index
     * @param y the second index
     * @param d the value to put in there.
     */
    private void putInIntIntMap(HashMap<Integer, HashMap<Integer, Double>> m, int x, int y, double d) {
        {
            HashMap yMap;

            if (m.containsKey(x)) {
                yMap = m.get(x);
            } else {
                yMap = new HashMap<Integer, Double>();
                m.put(x, yMap);
            }

            yMap.put(y, d);
        }
    }
}