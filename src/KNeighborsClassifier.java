import java.util.*;
import java.util.List;

public class KNeighborsClassifier {
    private int n_neighbors;
    private String metric;
    private String[][] X_Train;
    private String[] Y_Train;
    private List<Integer> results;
    private double accuracy;
    KNeighborsClassifier(int n_neighbors,String metric) throws Exception {
        if(n_neighbors%2==0) throw new Exception("Number of neighbors can't be even");
        this.n_neighbors=n_neighbors;
        this.metric=metric;
    }

    public void fit(String[][] X_Train, String[] Y_Train){
        this.X_Train=X_Train;
        this.Y_Train=Y_Train;
    }
    public void predict(String[][] X_Test){
        if(metric.equals("euclidean")) results=Classification(getNearestNeighborsClassification(calculateEuclidean(X_Test)));
        else if (metric.equals("manhattan")) results=Classification(getNearestNeighborsClassification(calculateManhattan(X_Test)));
        else System.out.println("Incorrect metric");
    }
    List<List<Integer>> getNearestNeighborsClassification(List<Map<Double,String>> SortedNeighbors){
        List<List<Integer>> NeighborsClassification = new ArrayList<>();
        for(int i=0;i<SortedNeighbors.size();++i){
            int k=n_neighbors;
            List<Integer> temp = new ArrayList<>();
            for (Map.Entry<Double, String> entry : SortedNeighbors.get(i).entrySet()) {
                if(k>0){
                    temp.add(Integer.parseInt(entry.getValue()));
                    k--;
                }
            }
            NeighborsClassification.add(temp);
        }
        return NeighborsClassification;
    }
    List<Map<Double,String>> calculateEuclidean(String[][] X_Test){
        List<Map<Double,String>> NeighborsList = new ArrayList<>();
        double sum;
        for(int i=0;i<X_Test.length;++i){
            Map<Double,String> Neighbors = new HashMap<>();
            for(int j=0;j<X_Test.length;++j){
                sum=0;
                for(int k=0;k<X_Test[0].length;++k){
                    sum+=Math.pow(Math.abs(Double.parseDouble(X_Test[i][k])-Double.parseDouble(X_Train[j][k])),2);
                }
                Neighbors.put(Math.sqrt(sum),Y_Train[j]);
            }
            sortingAndCuttingMapToNNeighbors(NeighborsList, Neighbors);
        }
        return NeighborsList;
    }

    private void sortingAndCuttingMapToNNeighbors(List<Map<Double, String>> neighborsList, Map<Double, String> neighbors) {
        Map<Double,String> SortedNeighbors = new TreeMap<>(neighbors);
        Map<Double, String> NSortedNeighbors = new LinkedHashMap<>();
        int count = n_neighbors;
        for (Map.Entry<Double, String> entry : SortedNeighbors.entrySet()) {
            if (count == 0) {
                break;
            }
            NSortedNeighbors.put(entry.getKey(), entry.getValue());
            count--;
        }
        neighborsList.add(NSortedNeighbors);
    }

    List<Map<Double,String>> calculateManhattan(String[][] X_Test){
        List<Map<Double,String>> NeighborsList = new ArrayList<>();
        double sum;
        for(int i=0;i<X_Test.length;++i){
            Map<Double,String> Neighbors = new HashMap<>();
            for(int j=0;j<X_Test.length;++j){
                sum=0;
                for(int k=0;k<X_Test[0].length;++k){
                    sum+=Math.abs(Double.parseDouble(X_Test[i][k])-Double.parseDouble(X_Train[j][k]));
                }
                Neighbors.put(sum,Y_Train[j]);
            }
            sortingAndCuttingMapToNNeighbors(NeighborsList, Neighbors);
        }
        return NeighborsList;
    }
    List<Integer> Classification(List<List<Integer>> NeighborsClassification){
        List<Integer> Classification = new ArrayList<>();
        for(List<Integer> PossibleClasses : NeighborsClassification){
            HashSet<Integer> PossibleClassesList = new HashSet<>(PossibleClasses);
            List<Integer> Counter = new ArrayList<>();
            for(Integer Class : PossibleClassesList){
                Counter.add(Collections.frequency(PossibleClasses,Class));
            }
            int MaxIndex = Counter.indexOf(Collections.max(Counter));
            Classification.add((Integer) PossibleClassesList.toArray()[MaxIndex]);
        }
        return Classification;
    }
    double calculate_accuracy(String[][] X_Test, String[] Y_Test){
        int correct=0;
        List<Integer> Results = results=Classification(getNearestNeighborsClassification(calculateEuclidean(X_Test)));
        for(int i=0;i<Results.size();++i){
            if(Results.get(i)==Integer.parseInt(Y_Test[i]))correct++;
        }
        accuracy = (double) correct /Results.size();
        return accuracy;
    }
    public List<Integer> getResults() {
        if(results.isEmpty()) return Collections.singletonList(-1);
        return results;
    }
}
