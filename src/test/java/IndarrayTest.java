import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

/**
 * Created by NF on 3/6/2017.
 */
public class IndarrayTest {
    public static void main(String[] args){

        int N=3;
        INDArray df1 = Nd4j.create(new int[]{N,N},'f');

        df1.putScalar(0,0,1);
        df1.putScalar(0,1,2);
        df1.putScalar(0,2,3);
        df1.putScalar(1,0,4);
        df1.putScalar(1,1,5);//0
        df1.putScalar(1,2,6);
        df1.putScalar(2,0,7);
        df1.putScalar(2,1,8);
        df1.putScalar(2,2,9);//0

        INDArray df2 = Nd4j.zeros(N+3,N+3,'f');

        System.out.println(df1);
        System.out.println();

        System.out.println(df2);
        System.out.println();

        df1.put(1,2,4);
        df1.putScalar(new int[]{1},3);
        System.out.println(df2.assign(df1));

    }
}
