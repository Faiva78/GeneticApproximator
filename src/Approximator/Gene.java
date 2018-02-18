/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Approximator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import javax.swing.JLabel;

/**
 *
 * @author Nicola
 */
public class Gene {

    //public String[] fomulae;
    Data dataList;

    Random rnd = new Random(System.currentTimeMillis());

    // init data
    private double outArr[][];

    // int numMax = 10;
    int steps = 30;
    double geneMinX = 0;
    double geneMaxX = 1;
    String geneFunc = "x";

    int numFormula = 1000;
    int maxLen = 32;

    double opsProb = 1;
    double varProb = 1;
    double numProb = 1;
    double funProb = 1;
    double divProb = 1;

    JLabel outLabel;

    double selectionPercent = 0.1;
    double mutationPercent = 0.5;
    double randVariation = 0.1;

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    // instatiation
    // init ops
    String[] ops = {"+", "-", "*", "/", "^"};
    String[] varConst = {"x", "pi", "e"};
    String[] fun = {"sin(", "cos(", "ln(", "log(", "exp(", "sqrt("};

    String[] divOpen = {"("};
    String[] divClose = {")"};

    String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};// initnum();
    String gg;

    ///
    // concatenate arrays
    ArrayList<String> arrOps = new ArrayList(Arrays.asList(ops));
    ArrayList<String> arrVar = new ArrayList(Arrays.asList(varConst));
    ArrayList<String> arrOpe = new ArrayList(Arrays.asList(divOpen));
    ArrayList<String> arrClo = new ArrayList(Arrays.asList(divClose));
    ArrayList<String> arrNum = new ArrayList(Arrays.asList(num));

    ArrayList<String> arrtot = new ArrayList();

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /// methods
    boolean getProb(double prob) {
        double pro = rnd.nextDouble();
        return pro < prob;
    }

    /**
     *
     * Need to add random function for avoiding local minima
     */
    public void mutate() {

        // concatenate all symbols
        arrtot.addAll(arrOps);
        arrtot.addAll(arrVar);
        arrtot.addAll(arrOpe);
        arrtot.addAll(arrClo);
        arrtot.addAll(arrNum);

        // data lenght
        int datalenght = dataList.SampleList.size();

        /// best 10% in tempdata
        Data bestData = new Data();

        int maxlist = dataList.SampleList.size() / (int) (selectionPercent * 100);

        for (int j = 0; j < maxlist; j++) {
            bestData.SampleList.add(dataList.SampleList.get(j));
        }

        // clear samplelist
        dataList.SampleList.clear();

        //int ranvar=  randVariation*100;
        int num2 = datalenght / (int) (randVariation * 100);
        int mutateList = datalenght - bestData.SampleList.size() - num2;

        // loop for max list minus the original best samples minus random formula
        // 
        for (int n = 0; n < mutateList;) {

            // take a random sample
            int idx = rnd.nextInt(bestData.SampleList.size());
            DataSample sample;
            sample = bestData.SampleList.get(idx);

            // random number of mutation
            int functionLenght = sample.function.length();
            int numMut = 0;
            do {
                numMut = (rnd.nextInt(functionLenght));
                double rate = (double) numMut / (double) functionLenght;
                if (rate <= mutationPercent) {
                    break;
                }
            } while (true);

            // temp formula
            String formula2 = "";

            // loop for number of mutations
            for (int t = 0; t < numMut; t++) {

                // get a random symbol
                String rand = arrtot.get(rnd.nextInt(arrtot.size()));

                // random string position
                int pos = rnd.nextInt(sample.function.length());

                // pick a random mutation type: add, substitute, remove
                int typeMut = rnd.nextInt(3);

                // switch type
                switch (typeMut) {
                    // add
                    case 0:
                        formula2 = sample.function.substring(0, pos) + rand + sample.function.substring(pos, sample.function.length());
                        break;

                    // sub
                    case 1:
                        StringBuffer buf = new StringBuffer(sample.function);
                        int start = pos;
                        int end = pos + 1;
                        buf.replace(start, end, rand); // Java Developers v1.4
                        formula2 = buf.toString();
                        break;

                    // remove
                    case 2:
                        StringBuilder sb = new StringBuilder(sample.function);
                        sb.deleteCharAt(pos);
                        formula2 = sb.toString();
                        break;

                } // end switch          
            } // end mutation number

            // check if empty
            if (formula2.isEmpty()) {
                continue;
            }

            // assign
            DataSample ee = new DataSample();
            ee.function = formula2;
            ee.error = Double.POSITIVE_INFINITY;
            dataList.SampleList.add(ee);

            outLabel.setText("Mutated " + (n * 100 / datalenght) + " %");
            n++;
        }

        // adding the original samples
        for (DataSample itm : bestData.SampleList) {
            dataList.SampleList.add(itm);
        }

        // adding random variation
        for (int f = 0; f < num2; f++) {
            DataSample da = new DataSample();
            da.function = addFormula(maxLen);
            dataList.SampleList.add(da);
        }

        outLabel.setText("Mutated " + datalenght);
    }
    //<editor-fold defaultstate="collapsed" desc="Old Mutate">
    ///////////////////// OLD MUTATE ///////////////////
//        int i = 0;
//        for (DataSample itm : dataList.SampleList) {
//
//            // take the random best
//            ////////////////// TODO //////////////
//            /////////////////////////////////////
//            // fix samples
//            ////
//            DataSample fo;
//
//            boolean exit = false;
//            do {
//                fo = dataList.SampleList.get(rnd.nextInt(dataList.SampleList.size()));
//                if (fo.error > 0) {
//                    exit = true;
//                }
//
//            } while (!exit);
//
//            // modify the fourmula
//            // number of mods
//            int numMut = (rnd.nextInt(fo.function.length()));
//            String formula2 = "";
//            // cycle for number of mutatuibs1
//            String formula;
//            formula = fo.function;
//
//            if (formula.length() <= 1) {
//                continue;
//            }
//
//            for (int j = 0; j < numMut; j++) {
//
//                // select a random value
//                String rand = arrtot.get(rnd.nextInt(arrtot.size()));
//                // selec a random position
//                if (formula.length() <= 1) {
//                    continue;
//
//                }
//
//                int pos = rnd.nextInt(formula.length());
//
//                // type of mutation: add, substitute, subtract
//                int typeMut = rnd.nextInt(3);
//                switch (typeMut) {
//
//                    //case ADD /////////////////////////////
//                    case 0:
//                        // add value
//                        //formula = fo.func;
//                        formula2 = formula.substring(0, pos) + rand + formula.substring(pos, formula.length());
//                        formula = formula2;
//                        //System.out.println("case:" + typeMut + " formula:" + formula + " rand:" + rand + " f2: " + formula2);
//                        break;
//
//                    //case substitute /////////////////////////////
//                    case 1:
//                        //formula = fo.func;
//                        StringBuffer buf = new StringBuffer(formula);
//
//                        int start = pos;
//                        int end = pos + 1;
//                        buf.replace(start, end, rand); // Java Developers v1.4
//                        formula = buf.toString();
//                        formula = formula2;
//                        break;
//                    //case remove/////////////////////////////
//                    case 2:
//
//                        // formula = fo.func;
//                        StringBuilder sb = new StringBuilder(formula);
//                        sb.deleteCharAt(pos);
//                        formula = sb.toString();
//                        formula = formula2;
//                        break;
//
//                }  //end switch
//            }
//            // assign a new funtion
//            DataSample ee = new DataSample();
//            ee.function = formula2;
//            bestData.SampleList.add(ee);
//
//            // System.out.println("new i:" + i + " = " + fo.function);
//            i++;
//        }
//
//        dataList.SampleList.clear();
//        dataList.SampleList.addAll(bestData.SampleList);
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="old evaluate thread">
    /**
     *
     * @param from
     * @param to
     * @deprecated
     */
    void evaluateRange(int from, int to) {
        
        System.out.println("From: " + from + " To:" + to);
        
        Thread thread = new Thread(() -> {
            try {
                /// can do multithreading
                //
                int totsamples = to - from;
                outLabel.setText("Starting evaluating");
                double[][] inData;//= new double[2][steps];
                inData = getFunctionData(gg);
                int cc = 0;
                for (int h = from; h <= to; h++) {
                    DataSample itm = dataList.SampleList.get(h);
                    double[][] testData; //= new double[2][steps];
                    double delta;
                    testData = getFunctionData(itm.function);
                    double sum = 0;
                    for (int i = 0; i < testData[0].length; i++) {
                        delta = Math.pow(inData[1][i] - testData[1][i], 2);
                        sum += Math.sqrt(delta);
                    }
                    itm.error = sum;
                    outLabel.setText("Evaluating " + (cc * 100 / totsamples) + " %");
                    cc++;
                }
                
            } catch (Exception e) {
                System.out.println((e.getMessage()));
            }
        });
        
        thread.start();
        
    }
//</editor-fold>

    /**
     * Start Multithreading evaluation
     *
     * @param threads
     */
    void evaluateThreads(int threads) {

        try {
            // divide the data load by threads
            int saml = numFormula;

            // nu,ner of samples x thread
            int divv = saml / threads;

            // initialize latch
            CountDownLatch latch = new CountDownLatch(threads);

            // thread loop
            for (int i = 0; i < threads; i++) {

                // calculate range
                int from = i * divv;
                int to = ((i + 1) * divv) - 1;
                // old function
                //evaluateRange(i * divv, ((i + 1) * divv) - 1);
                Thread thread = new Thread(() -> {
                    try {
                        int totsamples = to - from;
                        outLabel.setText("Starting evaluating");
                        double[][] inData;//= new double[2][steps];
                        inData = getFunctionData(gg);
                        int cc = 0;
                        for (int h = from; h <= to; h++) {
                            DataSample itm = dataList.SampleList.get(h);
                            double[][] testData; //= new double[2][steps];
                            double delta;
                            testData = getFunctionData(itm.function);
                            double sum = 0;
                            for (int l = 0; l < testData[0].length; l++) {
                                delta = Math.pow(inData[1][l] - testData[1][l], 2);
                                sum += Math.sqrt(delta);
                            }
                            itm.error = sum;
                            outLabel.setText("Evaluating " + (cc * 100 / totsamples) + " %");
                            cc++;
                        }
                        
                        latch.countDown();

                    } catch (Exception e) {
                        System.out.println((e.getMessage()));
                    }
                });
                thread.start();
            }
            // need to wait all threads
            latch.await();
            outLabel.setText("MT Evaluation completed");
            // sort
            dataList.SampleList.sort(new CompareDataSample());                    
        } catch (InterruptedException interruptedException) {           
        }
    }

    
    /**
     * Evaluate RMS function
     */
    public void evaluate() {

        /// can do multithreading
        // 
        outLabel.setText("Starting evaluating");
        double[][] inData = new double[2][steps];
        inData = getFunctionData(gg);
        int cc = 0;
        for (DataSample itm : dataList.SampleList) {

            double[][] testData = new double[2][steps];
            double delta;
            testData = getFunctionData(itm.function);
            double sum = 0;
            for (int i = 0; i < testData[0].length; i++) {
                delta = Math.pow(inData[1][i] - testData[1][i], 2);
                sum += Math.sqrt(delta);
            }
            itm.error = sum;
            outLabel.setText("Evaluating " + (cc * 100 / dataList.SampleList.size()) + " %");
            cc++;
        }

        // sort arraylist
        dataList.SampleList.sort(new CompareDataSample());
        outLabel.setText("Finished evaluating");
    }

    /**
     *
     * @param sample
     * @return
     */
    double[][] getFunctionData(String sample) {
        Evaluator funcEval = new Evaluator(sample);
        double diffStep = geneMaxX - geneMinX;
        double steppp = diffStep / steps;
        double acc = 0;
        double[][] out = new double[2][steps];
        for (int s = 0; s < steps; s++) {
            double ics = geneMinX + acc;
            double resuReferral = funcEval.evaluate(ics);
            out[0][s] = ics;
            out[1][s] = resuReferral;
            acc = acc + steppp;
        }
        return out;
    }

    public String addFormula(int maxRndgg) {
        boolean ready = false;
        StringBuilder str1;
        do {
            ready = true;
            int len = rnd.nextInt(maxRndgg);
            str1 = new StringBuilder();
            str1.append("(");

            for (int n = 0; n < len;) {
                // choose array
                int r = rnd.nextInt(6);
                switch (r) {

                    // OPS ///////////////////////
                    case 0:
                        // forbid open
                        if (!checkIfExistPrec(str1, divOpen)) {
                            // forbid ops
                            if (!checkIfExistPrec(str1, ops)) {

                                // calculate probabilitiy
                                if (!getProb(opsProb)) {
                                    break;
                                }

                                str1.append(ops[rnd.nextInt(ops.length)]);
                                n++;
                            }
                        }
                        break;

                    // VAR  /////////////////////
                    case 1:
                        // forbid close
                        if (!checkIfExistPrec(str1, divClose)) {
                            // forbid num
                            if (!checkIfExistPrec(str1, num)) {
                                // forbid var
                                if (!checkIfExistPrec(str1, varConst)) {

                                    // calculate probabilitiy
                                    if (!getProb(varProb)) {
                                        break;
                                    }

                                    str1.append(varConst[rnd.nextInt(varConst.length)]);
                                    n++;
                                }
                            }
                        }
                        break;

                    // FUN  /////////////////////
                    case 2:
                        // forbid close
                        if (!checkIfExistPrec(str1, divClose)) {
                            // forbid num
                            if (!checkIfExistPrec(str1, num)) {
                                // forbid var
                                if (!checkIfExistPrec(str1, varConst)) {

                                    // calculate probabilitiy
                                    if (!getProb(funProb)) {
                                        break;
                                    }

                                    str1.append(fun[rnd.nextInt(fun.length)]);
                                    n++;
                                }
                            }
                        }
                        break;

                    // NUM /////////////////////////
                    case 3:
                        //forbid close
                        if (!checkIfExistPrec(str1, divClose)) {
                            // forbid var,
                            if (!checkIfExistPrec(str1, varConst)) {

                                // calculate probabilitiy
                                if (!getProb(numProb)) {
                                    break;
                                }

                                str1.append(num[rnd.nextInt(num.length)]);
                                n++;
                            }
                        }
                        break;

                    // DIV open ///////////////////
                    case 4:
                        // forbid close
                        if (!checkIfExistPrec(str1, divClose)) {
                            // forbid num
                            if (!checkIfExistPrec(str1, num)) {
                                // forbid var
                                if (!checkIfExistPrec(str1, varConst)) {

                                    // calculate probabilitiy
                                    if (!getProb(divProb)) {
                                        break;
                                    }

                                    str1.append(divOpen[rnd.nextInt(divOpen.length)]);
                                    n++;
                                }
                            }
                        }
                        break;

                    // DIV close ////////////////////////
                    case 5:
                        // forbid ops
                        if (!checkIfExistPrec(str1, ops)) {
                            // forbid open
                            if (!checkIfExistPrec(str1, divOpen)) {
                                // check open
                                Stack stack = checkPar(str1);
                                if (!stack.isEmpty()) {

                                    // calculate probabilitiy
                                    if (!getProb(divProb)) {
                                        break;
                                    }

                                    str1.append(divClose[rnd.nextInt(divOpen.length)]);
                                    n++;
                                }
                            }
                        }
                        break;

                    //////
                    default:
                        str1.append("");

                }

            }

            // need to remove last: ops
            if (checkIfExistPrec(str1, ops)) {
                str1.setLength(str1.length() - 1);
            }

            // need to remove last: open
            if (checkIfExistPrec(str1, divOpen)) {
                str1.setLength(str1.length() - 1);
            }

            // need to close the open 
            //
            Stack ssta = checkPar(str1);
            for (int i = 0; i < ssta.size(); i++) {
                str1.append(")");
            }

            // check if empty
            if (str1.length() == 0) {
                ready = false;
            }

            // check if no variables inside
            int indx = str1.indexOf("x");
            if (indx == -1) {
                ready = false;
            }

        } while (!ready);

        // return
        return str1.toString();
    }

    private Stack checkPar(StringBuilder str1) {
        // must forbid if not also  open in string
        Stack stack = new Stack();
        for (int i = 0; i < str1.length(); i++) {
            String ss = str1.substring(i, i + 1);
            //System.out.println(ss);
            if ("(".equals(ss)) {
                stack.push(i);
            }
            if (")".equals(ss)) {
                stack.pop();
            }
        }
        return stack;
    }

    private boolean checkIfExistPrec(StringBuilder str1, String[] type) {
        boolean exist = false;
        for (int o = 0; o < type.length; o++) {
            // last char
            String test = str1.substring(str1.length() - 1);
            String t2 = type[o];
            if (test == null ? t2 == null : test.equals(t2)) {
                exist = true;
            }
        }

        return exist;
    }

    public void generateFormulae() {
        dataList.SampleList.clear();
        for (int i = 0; i < numFormula; i++) {
            DataSample data = new DataSample();
            data.function = addFormula(maxLen);
            dataList.SampleList.add(data);
            outLabel.setText("Calculating " + (i * 100 / numFormula) + " %");
        }
    }

    String displayformulas() {
        StringBuilder bu = new StringBuilder();
        for (DataSample itm : dataList.SampleList) {
            bu.append(itm.function).append("\n");
        }
        return bu.toString();
    }

    /**
     * *
     * Display the internal array data
     *
     * @param dbData
     * @return
     */
    //String dispaydata(double[][] dbData) {
    String dispaydata() {
        //char newlineChar = (char) 9; //tab
        String newlineChar = "\n"; //tab

        StringBuilder str = new StringBuilder();
        str.append("---------------------------").append(newlineChar).append("");
        str.append("---------------------------").append(newlineChar);
        int le = outArr[0].length;
        for (int x = 0; x < le; x++) {
            String a1 = String.valueOf(outArr[0][x]);
            String a2 = String.valueOf(outArr[1][x]);

            str.append(a1 + "        " + a2 + "        " + newlineChar);
        }
        String retu = str.toString();

        return retu;
    }

}
