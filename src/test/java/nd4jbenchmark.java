import core.MathUtil;
import org.apache.log4j.BasicConfigurator;
//import org.nd4j.linalg.api.blas.Lapack;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.buffer.util.DataTypeUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.Random;
//import org.nd4j.linalg.cpu.nativecpu.blas.CpuLapack;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.jcublas.blas.JcublasLapack;
import org.nd4j.linalg.jcublas.blas.JcublasLevel1;
import org.nd4j.nativeblas.Nd4jBlas;

/**
 * Created by NF on 3/4/2017.
 */
public class nd4jbenchmark {


    public static void main(String[] args) {

        BasicConfigurator.configure();
        int N = 3;
//        DataTypeUtil.setDTypeForContext(DataBuffer.Type.DOUBLE);
//        INDArray df1 = Nd4j.rand(N, N);
        INDArray df1 = Nd4j.create(new int[]{N,N},'f');
        putrands(df1,N);
//        df1=Nd4j.toFlattened('f',df1);
//        df1.putScalar(0,0,1);
//        df1.putScalar(0,1,2);
//        df1.putScalar(0,2,3);
//        df1.putScalar(1,0,4);
//        df1.putScalar(1,1,5);//0
//        df1.putScalar(1,2,6);
//        df1.putScalar(2,0,7);
//        df1.putScalar(2,1,8);
//        df1.putScalar(2,2,9);//0
        INDArray df2 = Nd4j.rand(N, 1);
        INDArray INFO = Nd4j.createArrayFromShapeBuffer(Nd4j.getDataBufferFactory().createInt(1L), Nd4j.getShapeInfoProvider().createShapeInformation(new int[]{1, 1}));
        INDArray IPIV = Nd4j.createArrayFromShapeBuffer(Nd4j.getDataBufferFactory().createInt((long)N), Nd4j.getShapeInfoProvider().createShapeInformation(new int[]{1, N}));
//        INDArray ipiv = Nd4j.rand(N,1);
//        INDarray df3 = Nd4j.rand

        double matrix[][] = new double[N][N];
        int pivot[] = new int[N];
        double b[] = new double[N];

        java.util.Random rn = new java.util.Random();
        for(int i=0;i<N;i++){
            pivot[i]= rn.nextInt(N);
        }


        cpymtr(df1, matrix, N);
        cpymar(df2, b, N);

//        System.out.println("Before");
//        System.out.println(df1);
//        printmtr(matrix,N);

        System.out.println("OMP_NUM_THREADS "+  System.getenv().get("OMP_NUM_THREADS"));

        long start = System.currentTimeMillis();

//        Nd4j.getBlasWrapper().lapack().getrf(df1);
        JcublasLapack jcub = new JcublasLapack();
//        CpuLapack cpul = new CpuLapack();

        jcub.sgetrf(N,N,df1,IPIV,INFO);
//        jcub.getrf(df1);
        MathUtil.lu_factor(matrix,N,pivot);
        MathUtil.lu_solve(matrix,N,pivot,b);

//        JCublas.


        System.out.println("Time Nd4j: " + (System.currentTimeMillis() - start) + " ");

        System.out.println(df1);
        printmtr(matrix,N);
////
////        System.out.println();
////
        System.out.println(IPIV);
        printar(pivot,N);


//        INDArray df3 = df1.dup();
//
//        DoubleMatrix df2 = new DoubleMatrix().rand(5000,5000);
//        DoubleMatrix df4 = df2.dup();
//
//        System.out.println(df1);
//
//        Nd4j.getBlasWrapper().lapack().getrf(df1);
//
//
//        System.out.println(df1);


//        INDArray df2 = Nd4j.rand(2,2);


//        System.out.println(df1);
//        InvertMatrix.invert(df1,true);
//        System.out.println(df1);
//        InvertMatrix.invert(df1,true);

//        long start = System.currentTimeMillis();
//        df2=df2.mmul(df4);
//        System.out.println( "Time JBLAS: "+ (System.currentTimeMillis()-start) +" "+  df2.get(0,0));

//        long start = System.currentTimeMillis();
//        df3=df3.mmul(df1);
//        System.out.println( "Time Nd4j: "+ (System.currentTimeMillis()-start) +" "+  df3.getDouble(0,0));


    }

    private static void putrands(INDArray a,int N){
        java.util.Random rn = new java.util.Random();
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                a.putScalar(i,j,rn.nextDouble());
            }
        }
    }

    private static void printmtr(double[][] matrix, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\r\n");
        }
    }

    private static void printar(double[] matrix, int N) {
        for (int j = 0; j < N; j++) {
            System.out.print(matrix[j] + " ");
        }
        System.out.print("\r\n");
    }

    private static void printar(int[] matrix, int N) {
        for (int j = 0; j < N; j++) {
            System.out.print(matrix[j] + " ");
        }
        System.out.print("\r\n");
    }

    private static void cpymtr(INDArray matfrom, double[][] matto, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matto[i][j] = matfrom.getDouble(i, j);
            }
        }
    }


    private static void cpymar(INDArray matfrom, double[] matto, int N) {
        for (int j = 0; j < N; j++) {
            matto[j] = matfrom.getDouble(j, 0);
        }
    }
}