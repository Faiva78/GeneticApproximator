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

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /// methods
    boolean getProb(double prob) {
        double pro = rnd.nextDouble();
        if (pro < prob) {
            return true;
        }
        return false;
    }

    public void mutate() {

        // concatenate arrays
        ArrayList<String> arrOps = new ArrayList(Arrays.asList(ops));
        ArrayList<String> arrVar = new ArrayList(Arrays.asList(varConst));
        ArrayList<String> arrOpe = new ArrayList(Arrays.asList(divOpen));
        ArrayList<String> arrClo = new ArrayList(Arrays.asList(divClose));
        ArrayList<String> arrNum = new ArrayList(Arrays.asList(num));

        ArrayList<String> arrtot = new ArrayList();

        arrtot.addAll(arrOps);
        arrtot.addAll(arrVar);
        arrtot.addAll(arrOpe);
        arrtot.addAll(arrClo);
        arrtot.addAll(arrNum);

        Data tempdata = new Data();

        /// for all formulas
        int i = 0;
        for (DataSample itm : dataList.SampleList) {

            // take the random best
            ////////////////// TODO //////////////
            /////////////////////////////////////
            // fix samples
            DataSample fo;

            boolean exit = false;
            do {
                fo = dataList.SampleList.get(rnd.nextInt(dataList.SampleList.size()));
                if (fo.error > 0) {
                    exit = true;
                }

            } while (!exit);

            // modify the fourmula
            // number of mods
            int numMut = (rnd.nextInt(fo.function.length()));
            String formula2 = "";
            // cycle for number of mutatuibs1
            String formula;
            formula = fo.function;
            if (formula.length() <= 1) {
                continue;
            }
            for (int j = 0; j < numMut; j++) {

                // select a random value
                String rand = arrtot.get(rnd.nextInt(arrtot.size()));
                // selec a random position
                if (formula.length() <= 1) {
                    continue;

                }
                int pos = rnd.nextInt(formula.length());
                // type of mutation: add, substitute, subtract
                int typeMut = rnd.nextInt(3);
                switch (typeMut) {

                    //case ADD /////////////////////////////
                    case 0:
                        // add value
                        //formula = fo.func;
                        formula2 = formula.substring(0, pos) + rand + formula.substring(pos, formula.length());
                        formula = formula2;
                        //System.out.println("case:" + typeMut + " formula:" + formula + " rand:" + rand + " f2: " + formula2);
                        break;

                    //case substitute /////////////////////////////
                    case 1:
                        //formula = fo.func;
                        StringBuffer buf = new StringBuffer(formula);

                        int start = pos;
                        int end = pos + 1;
                        buf.replace(start, end, rand); // Java Developers v1.4
                        formula = buf.toString();
                        formula = formula2;
                        break;
                    //case remove/////////////////////////////
                    case 2:

                        // formula = fo.func;
                        StringBuilder sb = new StringBuilder(formula);
                        sb.deleteCharAt(pos);
                        formula = sb.toString();
                        formula = formula2;
                        break;

                }  //end switch
            }
            // assign a new funtion
            DataSample ee = new DataSample();
            ee.function = formula2;
            tempdata.SampleList.add(ee);

            System.out.println("new i:" + i + " = " + fo.function);
            i++;
        }

        dataList.SampleList.clear();
        dataList.SampleList.addAll(tempdata.SampleList);
    }

    public void evaluateRmsFormulae() {

        double[][] Gen = new double[2][steps];
        Gen = getFunctionData(gg);

        int cc = 0;
        for (DataSample itm : dataList.SampleList) {

            double[][] tes = new double[2][steps];
            double delta;

            tes = getFunctionData(itm.function);
            double sum = 0;
            for (int i = 0; i < tes[0].length; i++) {

                delta = Math.pow(Gen[1][i] - tes[1][i], 2);
                sum += Math.sqrt(delta);
            }
            itm.error = sum;
            System.out.println("func " + cc + " err:" + itm.error);

            cc++;
        }

    }

    
    
    
    public void evaluateRmsFormulae2() {
        StringBuilder out = new StringBuilder();
        double bestRms = Double.POSITIVE_INFINITY;
        String bestF = "";

        int i = 0;
        for (DataSample itm : dataList.SampleList) {

            String thisFun = itm.function;
            Evaluator funcToeval = new Evaluator(itm.function);
            Evaluator funcReferral = new Evaluator(geneFunc);

            double diffStep = geneMaxX - geneMinX;
            double steppp = diffStep / steps;
            double acc = 0;
            double error = 0;

            for (int s = 0; s < steps; s++) {

                double ics = geneMinX + acc;

                double resuEval = funcToeval.evaluate(ics);
                double resuReferral = funcReferral.evaluate(ics);

                double delta = Math.abs(resuReferral - resuEval);

                error = error + delta; //Math.pow(delta, 2);
                acc = acc + steppp;
            }
            itm.error = error;
            i++;
        }

    }

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
        //funclist.clear();
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

    void generateFormulae() {
        dataList.SampleList.clear();
        for (int i = 0; i < numFormula; i++) {
            DataSample data = new DataSample();
            data.function = addFormula(maxLen);
            dataList.SampleList.add(data);
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
