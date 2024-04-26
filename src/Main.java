public class Main {
    public static void main(String[] args) throws Exception {
        DataReader dataReader1 = new DataReader("train_data_cancer.txt");
        String[][] X_Train = dataReader1.getDataWithoutLabels();
        String[] Y_Train = dataReader1.getLabels();

        DataReader dataReader2 = new DataReader("test_data_cancer.txt");
        String[][] X_Test = dataReader2.getDataWithoutLabels();
        String[] Y_Test = dataReader2.getLabels();

        for(int i=3;i<=11;i+=2){
            KNeighborsClassifier knn = new KNeighborsClassifier(i, "euclidean");
            knn.fit(X_Train, Y_Train);
            knn.predict(X_Test);
            System.out.println(knn.calculate_accuracy(X_Test, Y_Test));
        }
        System.out.println();
        for(int i=3;i<=11;i+=2){
            KNeighborsClassifier knn = new KNeighborsClassifier(i, "manhattan");
            knn.fit(X_Train, Y_Train);
            knn.predict(X_Test);
            System.out.println(knn.calculate_accuracy(X_Test, Y_Test));
        }
    }
}
