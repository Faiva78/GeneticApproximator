/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Approximator;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author alessia
 */
public class TableDataModelFunctions extends AbstractTableModel {

    private final String[] ColName = new String[]{"Function", "error"};
    public Data data;

    public TableDataModelFunctions(Data data) {
        this.data = data;
    }

    public TableDataModelFunctions() {
    }

    /**
     * @deprecated 
     * @param datas
     * @return 
     */
    private String getValues(double[] datas){
        
            String str="";
            for (int i = 0; i < datas.length; i++) {
                double indata = datas[i];
                str += String.valueOf(indata)+",";
            }
            str= str.substring(0, str.length()-1);
            return  str;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0) {
            
            return  (data.SampleList.get(rowIndex).function);

            
        }
        if (columnIndex == 1) {
           return  (data.SampleList.get(rowIndex).error);
        }

        throw new UnsupportedOperationException("column not existant."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //return super.isCellEditable(rowIndex, columnIndex); //To change body of generated methods, choose Tools | Templates.
        
        return (rowIndex < data.SampleList.size()) && (columnIndex < ColName.length);
    }

    @Override
    public int getColumnCount() {
        return ColName.length;
      
    }

    @Override
    public int getRowCount() {
        return data.SampleList.size();
      
    }

    @Override
    public String getColumnName(int column) {

        return ColName[column];
        //return super.getColumnName(column); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (data.SampleList.isEmpty()) {
            return  Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
        //return super.getColumnClass(columnIndex); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    private double[] stringToArray(String str) {

        String[] strArr = str.split(",");
        double[] ret = new double[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            String ret1 = strArr[i];
            double val = Double.valueOf(ret1);
            ret[i] = val;
        }
        return ret;

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {

            if (aValue.getClass() == String.class) {
                if (columnIndex == 0) {
                    data.SampleList.get(rowIndex).function = (String) aValue;
                } else if (columnIndex == 1) {
                    data.SampleList.get(rowIndex).error = (Double) aValue;
                }
                fireTableCellUpdated(rowIndex, columnIndex);
            }
            
        } catch (Exception e) {
            System.out.println("error  in setting value in table model:" +e.getMessage());
        }
        super.setValueAt(aValue, rowIndex, columnIndex); //To change body of generated methods, choose Tools | Templates.

    }

}
