/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Approximator;


import org.nfunk.jep.JEP;

/**
 *
 * @author Nicola
 */
public class Evaluator {
       /** Derivation precision*/
    public double derivationPrecision = 0.001;
    /** Integration precision*/
    public double integrationPrecision = 0.01;
    
    /**  string of the formula;*/
    public String formlua;
    
    /** Parser of the formula*/
    private final JEP parser =new JEP();

    public Evaluator() {
            formlua="x";
        initParser();
    }

    

    public Evaluator(String formula) {
        formlua=formula;
        initParser();
    }

    private void initParser(){
     parser.addStandardConstants();
     parser.addStandardFunctions();
    
    }
    
    
    /**Return the function value f(x)
     * @param x paraneter to be evaluated
     * @return x  return value*/
    
    public double evaluate(double x) {
        parser.addVariable("x", x);
        parser.parseExpression(formlua);
        return  parser.getValue();
        //return MathParser.eval(formlua, new double[]{x});
    }
    
    
    

    /**Return the function value f(x,y)
     @param x  paraneter x to be evaluated
     @param y  paraneter y to be evaluated
     * @return the value evaluated*/
    public double evaluate(double x, double y) {
        parser.addVariable("x", x);
        parser.addVariable("y", y);
        parser.parseExpression(formlua);
        return  parser.getValue();
    }
    
    
    public double summation(double x,int from,int to){
        double sum=0;
        for (int i = from; i < to; i++) {
            sum=sum+evaluate(x,i);
        }
    return 0;
    }
    
    
    
    
    /** Return the derivative of f(x) 
     @param x paraneter x to be evaluated
     * @return */
    public double derivation(double x) {
        double dx = evaluate(x);
        double dx1 = evaluate(x + derivationPrecision);
        return (dx - dx1) / derivationPrecision;
    }
    /** Return the integration f the tunction from a to b 
     @param a start of the limmit
     @param b end of the limit
     * @return */
    public double integration(double a, double b) {
        double sum = 0;
        double step = (b - a) * integrationPrecision;
        for (double i = a; i < b; i = i + step) {
            double eva = evaluate(i);
            sum = sum + eva;
        }
        return sum;
    }

    /** Return the partial derivative of f(x,y) with respect to x = Df/Dx
     @param x paraneter x to be evaluated
     @param y paraneter x to be evaluated
     * @return */
    public double partial_x(double x, double y) {
        double dx = evaluate(x, y);
        double dx1 = evaluate(x + derivationPrecision, y);
        return (dx - dx1) / derivationPrecision;
    }
    /** Return the partial derivative of f(x,y) with respect to y = Df/Dy
     @param x paraneter x to be evaluated
     @param y paraneter x to be evaluated
     * @return */
    public double partial_y(double x, double y) {
        double dx = evaluate(x, y);
        double dx1 = evaluate(x, y + derivationPrecision);
        return (dx - dx1) / derivationPrecision;
    }
    /** Return the second partial derivative of fx(x,y) with respect to x  = Dfx/Dx
     @param x paraneter x to be evaluated
     @param y paraneter x to be evaluated
     * @return */
    public double second_partial_xx(double x, double y) {
        double dx = partial_x(x, y);
        double dx1 = partial_x(x + derivationPrecision, y);
        return (dx - dx1) / derivationPrecision;
    }
    /** Return the second partial derivative of fx(x,y) with respect to y  = Dfx/Dy
     @param x paraneter x to be evaluated
     @param y paraneter x to be evaluated
     * @return */
    public double second_partial_xy(double x, double y) {
        double dx = partial_x(x, y);
        double dx1 = partial_x(x, y + derivationPrecision);
        return (dx - dx1) / derivationPrecision;
    }
    /** Return the second partial derivative of fy(x,y) with respect to x  = Dfy/Dx
     @param x paraneter x to be evaluated
     @param y paraneter x to be evaluated
     * @return */
    public double second_partial_yx(double x, double y) {
        double dx = partial_y(x, y);
        double dx1 = partial_y(x + derivationPrecision, y);
        return (dx - dx1) / derivationPrecision;
    }
    /** Return the second partial derivative of fy(x,y) with respect to y  = Dfy/Dy
     @param x paraneter x to be evaluated
     @param y paraneter x to be evaluated
     * @return */
    public double second_partial_yy(double x, double y) {
        double dx = partial_y(x, y);
        double dx1 = partial_y(x, y + derivationPrecision);
        return (dx - dx1) / derivationPrecision;
    }


    public double gradient() {
        double x_old = 0;
        double x_new = 6;
        double gamma = 0.001;
        double precision = 0.00001;
        while ((x_new - x_old) > precision) {
            x_old = x_new;
            double deri = derivation(x_old);
            x_new = x_old - (gamma * deri);
        }
        return x_new;
    }

    
    static double meanError(double x, double fx, double d) {
        double diffs = fx - x;
        double pow = Math.pow(diffs, 2);
        double error = pow / d;
        return error;
    }

    /**
     * Calculate the squarew mean error
     *
     * @param x deidered output
     * @param fx function output
     * @return  square mean error
     */
    static double meanError(double x, double fx) {
        return meanError(x, fx, 2);
    }

}
