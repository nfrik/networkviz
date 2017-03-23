import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NF on 3/1/2017.
 */
public class nd4jtest {
    public static void main(String[] args) {

//        INDArray a1 = Nd4j.rand(10000,10000);
//        INDArray a2 = Nd4j.eye(10000);
//        long start = System.currentTimeMillis();
////        a1=a1.mmul(a1);
//        System.out.println("Started");
//        for(int i = 0;i<10;i++){
//            a1.mmul(a1);
//        }
////        InvertMatrix.invert(a1,true);
//        System.out.println("Time taken: "+(System.currentTimeMillis()-start)+"                    "+a1.getDouble(0,0));

        List<Double> list = new ArrayList<Double>();

        list.add(1.);
        list.add(2.);
        list.add(3.);


        double sum = list.stream().mapToDouble(d->d).sum();

        int size = list.size();
        list = list.stream().map(x -> x * x / size).collect(Collectors.toList());


        System.out.println("Sum "+sum);

        System.out.println(list);

        System.out.format("Blah %2.2f",3445.);
        System.out.println(String.format("Blah %2.2f",3445.));
    }

}
