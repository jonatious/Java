package clustering;
import java.io.*;
import java.util.*;

public class Clustering {
    public static String accuracy_out = "";
    public static boolean trigger = false;
    public static void main(String[] args) {
        String train_file_Name = "C:\\Users\\jonyhero\\Desktop\\train.csv";
        String test_file_Name = "C:\\Users\\jonyhero\\Desktop\\test.csv";
        int counter;
        
        Object training[] = Read(train_file_Name);
        Object testing[] = Read(test_file_Name);
        
        //no of points
        int points = (int)training[0];
        int test_points = (int)testing[0];
        
        //Attribute names
        ArrayList attr_names = (ArrayList)training[1];
        
        //Class name
        String class_name = (String)training[2];
        
        //Atribute data and class values
        ArrayList<double[]> attr_data = (ArrayList<double[]>)training[3];
        ArrayList class_value = (ArrayList)training[4];
        HashSet unique_classes = (HashSet)training[5];
        
        //Atribute data and class values -- testing
        ArrayList<double[]> attr_data_test = (ArrayList<double[]>)testing[3];
        ArrayList class_value_test = (ArrayList)testing[4];
        
        //finding accuracy for all possible k values
        double[] accuracy = new double[points/2 - unique_classes.size() + 1];
        
        for(int i=unique_classes.size(); i<unique_classes.size()*20 ; i++){
        //int i = 6;
            int[] chosen_mean = new int[points];
            int[] chosen_mean_test = new int[test_points];
            //generating i rand points
            double[][] means = new double[i][attr_names.size()];
            String[] means_class = new String[i];
            for (int j=0; j<i; j++){
                for(int k =0; k<attr_names.size(); k++)
                    means[j][k] = random(attr_data,k);
            }
            
            double error = 1;
            double[][] new_means = new double[i][attr_names.size()];
            //iterating to find the best means
            counter = 0;
            while(counter < 300){
                error = 0;
                //classification of points
                for(int j=0; j< points; j++){
                    double distance = distance(attr_data.get(j),means[0]);
                    chosen_mean[j] = 0;
                    for(int k=1; k<i; k++){
                        double temp_dist = distance(attr_data.get(j),means[k]);
                        if(temp_dist < distance){
                            distance = temp_dist;
                            chosen_mean[j] = k;
                        }
                    }
                }
                
                //class of means
                for(int j=0; j<i; j++){
                    means_class[j] = majority(chosen_mean, j, class_value, unique_classes.toArray());
                }
                
                //re generating means
                for(int t=0; t<i; t++){
                    for(int j=0; j<attr_names.size(); j++){
                        
                        double sum = 0;
                        double count = 0;
                        for(int k=0; k<points; k++)
                            if(chosen_mean[k] == t ){
                                sum += attr_data.get(k)[j] * similarity(t,k,means_class,class_value);
                                count += 1 * similarity(t,k,means_class,class_value);
                            }
                        new_means[t][j] = sum/count;
                    }
                    error += distance(new_means[t],means[t]);
                }
                
                for(int j=0; j<i; j++){
                    for(int k=0; k<attr_names.size(); k++){
                        means[j][k] = new_means[j][k];
                        //System.out.print(means[j][k] +",");
                    }
                        //System.out.print(means_class[j] +",");
                    
                }
                    //System.out.println();
                counter++;
            }
                //System.out.println("error = " + error);
                //System.out.println("counter = " + counter);
            
            //Testing accuracy
            int correct_classified = 0;
            for(int j=0; j<test_points; j++){
                double distance = distance(attr_data_test.get(j),means[0]);
                chosen_mean_test[j] = 0;
                for(int k=1; k<i; k++){
                    double temp_dist = distance(attr_data_test.get(j),means[k]);
                    if(temp_dist < distance){
                        distance = temp_dist;
                        chosen_mean_test[j] = k;
                    }
                }
                if(means_class[chosen_mean_test[j]].equals(class_value_test.get(j).toString()))
                    correct_classified++;
            }
            
            accuracy[i - unique_classes.size()] = (double)correct_classified/(double)test_points;
            
            if(i>unique_classes.size()){
                if(accuracy[i - unique_classes.size()] < accuracy[i - unique_classes.size() - 1])
                    trigger = true;
            }
            
            if(trigger && accuracy[i - unique_classes.size()] > accuracy[i - unique_classes.size() - 1])
                break;
                
            float temp = (float) (accuracy[i - unique_classes.size()]*100);
            accuracy_out += temp + ",";
            System.out.printf("Accuracy = %.2f", accuracy[i - unique_classes.size()]*100);
            System.out.println("%");
           
        }
        
        float max = 0;
        int k =0;
        for(int i=0; i<accuracy.length-1; i++){
            if(max<accuracy[i]){
                max = (float)accuracy[i];
                k = i+unique_classes.size();
            }
        }
        System.out.println("Max Accuracy = " + max*100 + "%\nThe best k value = " + k);
        try{
                PrintWriter write_acc = new PrintWriter("accuracy.csv", "UTF-8");
                write_acc.println(accuracy_out);
                write_acc.close();
            }
            catch(Exception e){
                System.out.println("Writing to file error : " + e);
            }
        
    }
    
    public static Object[] Read(String file_name){
        Object input[] = DataAccessLayer.read_file(file_name);
        if(input == null)
                return null;
        String[] attr_names_temp = (String[])input[0];
        double[][] attr_data_temp = (double[][])input[2];
        String[] class_value_temp = (String[])input[3];

        //no of points
        int points = 1;
        while(class_value_temp[points] != null)
            points++;
        points--;
        
        //Attribute names
        ArrayList attr_names = new ArrayList();
        
        for(int i=0; attr_names_temp[i] != null; i++)
            attr_names.add(attr_names_temp[i]);
        
        //Class name
        String class_name = (String)input[1];
        
        //Atribute data and class values
        ArrayList<double[]> attr_data = new ArrayList<double[]>();
        ArrayList class_value = new ArrayList();
        HashSet unique_classes = new HashSet();
        
        for(int j=1; j<=points; j++){
            double[] temp = new double[attr_names.size()];
            for(int i=0; i<attr_names.size(); i++)
                temp[i] = attr_data_temp[j][i];
            attr_data.add(temp);
            class_value.add(class_value_temp[j]);
            unique_classes.add(class_value_temp[j]);
        }
        return new Object[]{points, attr_names, class_name, attr_data, class_value, unique_classes};
    }
    
    public static double random(ArrayList<double[]> values, int index){
        return (Math.random()*(max(values, index) - min(values, index))) + min(values, index);
    }
    
    public static double distance(double[] values, double[] mean){
        double sum =0.0;
        for(int i=0; i<values.length; i++){
            sum += ((values[i] - mean[i])*(values[i] - mean[i]));
        }
        return Math.sqrt(sum);
    }
    
    public static String majority(int[] chosen_mean, int index, ArrayList class_value, Object[] class_unique){
        ArrayList temp_classes = new ArrayList();
        for(int i=0; i< chosen_mean.length; i++)
            if(chosen_mean[i] == index)
                temp_classes.add(class_value.get(i));
        int size = Collections.frequency(temp_classes, class_unique[0]);
        String majority = class_unique[0].toString();
        for(int i=1; i< class_unique.length; i++){
            int temp_size = Collections.frequency(temp_classes, class_unique[i]);
            if(size < temp_size){
                size = temp_size;
                majority = class_unique[i].toString();
            }
        }
        return majority;
    }
    
    public static double avg(ArrayList<double[]> values, int index){
        double sum = 0.0;
        for(int i=0; i<values.size();i++ )
            sum += values.get(i)[index];
        double avg = sum/(double)values.size();
        return avg;
    }
    
    public static double min(ArrayList<double[]> values, int index){
        double min_val = values.get(0)[index];
        for(int i=1; i<values.size();i++ )
            if(min_val>values.get(i)[index])
                min_val = values.get(i)[index];
        return min_val;
    }
    
    public static double max(ArrayList<double[]> values, int index){
        double max_val = values.get(0)[index];
        for(int i=1; i<values.size();i++ )
            if(max_val<values.get(i)[index])
                max_val = values.get(i)[index];
        return max_val;
    }

    private static double similarity(int t, int k,String[] means_class, ArrayList class_value) {
        if(means_class[t].equals(class_value.get(k)))
            return 1;
        else
            return 0;
    }
    
}
