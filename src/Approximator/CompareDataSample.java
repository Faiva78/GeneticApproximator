/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Approximator;

import java.util.Comparator;

/**
 *
 * @author Nicola
 */
public class CompareDataSample  implements  Comparator<DataSample>{

                     @Override
                        public int compare(DataSample o1, DataSample o2) {

                            double in1 = o1.error;
                            double in2 = o2.error;

                            /// remove Nan and neg infinity
                            if (Double.isNaN(in1)) {
                                in1 = Double.POSITIVE_INFINITY;
                            }
                            if (Double.isNaN(in2)) {
                                in2 = Double.POSITIVE_INFINITY;
                            }

                            if (in1 > in2) {
                                return 1;
                            } else if (in1 < in2) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
        
    
}
