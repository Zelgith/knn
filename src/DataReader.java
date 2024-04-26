import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.addAll;
import static java.util.Collections.checkedSortedMap;

public class DataReader {
    private List<List<String>> data;
    DataReader(String filename){
        try {
            File file = new File(filename);
            Scanner fileReader = new Scanner(file);
            this.data = new ArrayList<>();
            int hasID = 0;
            while(fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                List<String> dataArray = new ArrayList<>(List.of(line.split(";")));
                if(hasID==1)dataArray.remove(0);
                if(dataArray.get(0).equals("id")){
                    hasID = 1;
                    dataArray.remove(0);
                }
                data.add(dataArray);
            }
            fileReader.close();
        } catch (FileNotFoundException e){
            System.out.println("Couldn't find file named: "+ filename);
        }
    }
    private int getIndexOfLabel(){
        int index;
        if(data.get(0).contains("diagnosis")){
            index = data.get(0).indexOf("diagnosis");
        }else if(data.get(0).contains("species")){
            index = data.get(0).indexOf("species");
        }else{
            System.out.println("Couldn't find label");
            return -1;
        }
        return index;
    }
    public String[] getLabels(){
        String[] Labels = new String[data.size()-1];
        int index = getIndexOfLabel();
        for(int i=1;i<data.size();++i){
            Labels[i-1] = data.get(i).get(index);
        }
        return Labels;
    }
    public String[][] getDataWithoutLabels(){
        int index = getIndexOfLabel();
        List<List<String>> ModifiedData = new ArrayList<>();
        for(int i=0;i<data.size();++i){
            List<String> temp = new ArrayList<>();
            for(int j=0;j<data.get(0).size();++j) {
                if (j != index) {
                    temp.add(data.get(i).get(j));
                }
            }
            ModifiedData.add(temp);
        }
        String[][] DataWithoutLabel = new String[ModifiedData.size()-1][ModifiedData.get(0).size()];
        for(int i=1;i<ModifiedData.size();++i){
            for(int j=0;j<ModifiedData.get(0).size();++j) {
                DataWithoutLabel[i-1][j] = ModifiedData.get(i).get(j);
            }
        }
        return DataWithoutLabel;
    }
    public List<List<String>> getData() {
        return data;
    }
}
