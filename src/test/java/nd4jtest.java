import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.inverse.InvertMatrix;

/**
 * Created by NF on 3/1/2017.
 */
public class nd4jtest {
    public static void main(String [] args){

        INDArray a1 = Nd4j.rand(10000,10000);
        INDArray a2 = Nd4j.eye(10000);
        long start = System.currentTimeMillis();
//        a1=a1.mmul(a1);
        System.out.println("Started");
        for(int i = 0;i<10;i++){
            a1.mmul(a1);
        }
//        InvertMatrix.invert(a1,true);
        System.out.println("Time taken: "+(System.currentTimeMillis()-start)+"                    "+a1.getDouble(0,0));
    }

}
