package diagramvisualizer.model;

/**
 *
 * @author Milán
 */
public class Matrix {

    // az ellenőrzéshez ezt használtam: http://www.bluebit.gr/matrix-calculator/default.aspx
    private double[][] A;
    private int column;
    private int row;
    private static final int ROUND = 3;

    public Matrix(double[][] A) {
        this.A = A;
        //elenőrizni kell, hogy mindensorban ua column van-e, és ha nem nullázni
        this.column = A[0].length;
        this.row = A.length;
    }

    public Matrix(int a) {
        //mivan ha a<=0?
        this.A = new double[a][a];
        this.column = A[0].length;
        this.row = A.length;
    }

    public Matrix(int a, int b) {
        //mivan ha a<=0?
        //mivan ha b<=0?
        this.A = new double[a][b];
        this.row = a;
        this.column = b;
    }

    public double[][] getMatrix() {
        return A;
    }

    public double getMatrix(int row, int column) {
        return this.A[row][column];
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setMatrix(int row, int column, double value) {
        this.A[row][column] = value;
    }

    public void getMatrixConsole() {

        double value;
        int max = 0;
        String out = "";

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int length = String.valueOf(Math.round(A[i][j] * Math.pow(10, ROUND)) / (Math.pow(10, ROUND))).length();
                if (length > max) {
                    max = length;
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                value = Math.round(A[i][j] * Math.pow(10, ROUND)) / (Math.pow(10, ROUND));
                int ig = max - String.valueOf(value).length();
                for (int k = 0; k < ig; k++) {
                    out += " ";
                }
                out += String.valueOf(value);
                System.out.print(out + " ");
                out = "";
            }
            System.out.println();
        }
    }

    public void setMatrixRnd(int a) {
        //mivan ha a 0?
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                A[i][j] = (int) (Math.random() * (a)) + 1;
            }
        }
    }

    public void setMatrixRnd(int a, int b) {

        if (a > b) {
            int s = a;
            a = b;
            b = s;
        }

        if (Double.compare(a, b) == 0) {
            this.setMatrixRnd(a);
        } else {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    A[i][j] = (int) (Math.random() * (b - a + 1)) + a;
                }
            }
        }
    }

    public void setMatrixRnd(double a) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                A[i][j] = Math.random() * (a);
            }
        }
    }

    public void setMatrixRnd(double a, double b) {

        if (a > b) {
            double s = a;
            a = b;
            b = s;
        }

        if (Double.compare(a, b) == 0) {
            this.setMatrixRnd(a);
        } else {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    A[i][j] = (Math.random() * (b - a)) + a;
                }
            }
        }
    }

    public Matrix getSignMatrix(int a) {
        Matrix sign = new Matrix(a);

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < a; j++) {
                sign.setMatrix(i, j, Math.pow(-1, i + j));
            }
        }
        return sign;
    }

    public Matrix Summation(Matrix M) {
        double[][] B = M.getMatrix();
        double[][] AB = new double[row][column];

        if (row != M.row || column != M.column) {
            AB = this.A;
        } else {
            for (int i = 0; i < AB.length; i++) {
                for (int j = 0; j < AB[0].length; j++) {
                    AB[i][j] = A[i][j] + B[i][j];
                }
            }
        }

        Matrix out = new Matrix(AB);
        return out;
    }

    public Matrix Multiplication(Matrix M) {
        double[][] B = M.getMatrix();
        double[][] AB;

        if (column != M.row) {
            AB = this.A;
        } else {
            AB = new double[row][M.column];
            double sum = 0;

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < M.column; j++) {
                    for (int p = 0; p < column; p++) {
                        sum = sum + A[i][p] * B[p][j];
                    }
                    AB[i][j] = sum;
                    sum = 0;
                }
            }
        }
        Matrix out = new Matrix(AB);
        return out;
    }

    public Matrix Multiplication(double k) {
        Matrix out = new Matrix(A);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                out.setMatrix(i, j, k * A[i][j]);
            }
        }
        return out;
    }

    public Matrix getViceDeterminant(int row, int column) {
        double[][] vice = new double[A.length - 1][A.length - 1];
        if (row > this.row || column > this.column || this.column != this.row) {
            vice = A;
        } else {
            int k = 0;
            int l = 0;
            for (int i = 0; i < vice.length; i++) {
                k = i < row ? i : i + 1;
                for (int j = 0; j < vice.length; j++) {
                    l = j < column ? j : j + 1;
                    vice[i][j] = A[k][l];
                }
            }
        }
        Matrix out = new Matrix(vice);
        return out;
    }

    public Matrix getTransponent() {
        Matrix out = new Matrix(column, row);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                out.setMatrix(j, i, A[i][j]);
            }
        }
        return out;
    }

    public double getDeterminant() {
        double determinant = 0;
        int row = 0;
        //ekszepsön kéne
        if (column != row) {
            determinant = 0;
        }

        switch (row) {
            case 0:
                determinant = 0;
                break;
            case 1:
                determinant = A[0][0];
                break;
            case 2:
                determinant = A[0][0] * A[1][1] - A[1][0] * A[0][1];
                break;
            default:
                Matrix plusMinus = getSignMatrix(A.length);
                for (int i = 0; i < A.length; i++) {
                    Matrix m = getViceDeterminant(row, i);
                    determinant += A[row][i] * plusMinus.getMatrix(row, i) * m.getDeterminant();
                }
                break;
        }
        return determinant;
    }

    public Matrix getInvers() {
        Matrix original = new Matrix(A);
        Matrix adjung = new Matrix(A.length);
        Matrix transzponent, invers, plusMinus;

        double det = original.getDeterminant();

        double temp;
        plusMinus = getSignMatrix(A.length);

        if (Double.doubleToRawLongBits(original.getDeterminant()) == 0) {
            return original;
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                temp = plusMinus.getMatrix(i, j) * original.getViceDeterminant(i, j).getDeterminant();
                adjung.setMatrix(i, j, temp);
            }
        }
        transzponent = adjung.getTransponent();
        invers = transzponent.Multiplication(1 / (det));
        return invers;
    }

    public Matrix getGauss() {
        double[][] g;
        boolean transz = false;
        //ha több sora van mint oszlopa akkor "transzponálom"

        if (row > column) {
            //fura de ezt csak így tudtam megoldani
            g = new double[column][row];
            transz = true;
            //fura de ezt csak így tudtam megoldani
            for (int i = 0; i < g.length; i++) {
                for (int j = 0; j < g[0].length; j++) {
                    g[i][j] = A[j][i];
                }
            }
        } else {
            g = new double[row][column];
            //fura de ezt csak így tudtam megoldani
            for (int i = 0; i < g.length; i++) {
                for (int j = 0; j < g[0].length; j++) {
                    g[i][j] = A[i][j];
                }
            }
        }

        //netről: http://martin-thoma.com/solving-linear-equations-with-gaussian-elimination/#tocAnchor-1-2
        for (int i = 0; i < g.length; i++) {
            // Search for maximum in this column
            double columnMax = Math.abs(g[i][i]);
            int maxRow = i;
            for (int k = i + 1; k < g.length; k++) {
                if (Math.abs(g[k][i]) > columnMax) {
                    columnMax = Math.abs(g[k][i]);
                    maxRow = k;
                }
            }

            // Swap maximum row with current row (column by column)
            for (int k = i; k < g[0].length; k++) {
                double tmp = g[maxRow][k];
                g[maxRow][k] = g[i][k];
                g[i][k] = tmp;
            }

            // Make all rows below this one 0 in current column
            for (int k = i + 1; k < g.length; k++) {
                double c = -1 * g[k][i] / g[i][i];
                for (int j = i; j < g[0].length; j++) {
                    if (i == j) {
                        g[k][j] = 0;
                    } else {
                        g[k][j] = +g[k][j] + c * g[i][j];

                    }
                }
            }
        }

        Matrix out = new Matrix(g);
        if (transz) {
            out = out.getTransponent();
        }

        return out;
    }

    public Matrix getFullGauss() {
        Matrix out = this.getGauss();
        boolean transponent = false;

        if (row > column) {
            out = out.getTransponent();
            transponent = true;
        }

        double[][] fullGauss = new double[out.getRow()][out.getColumn()];
        for (int i = 0; i < fullGauss.length; i++) {
            for (int j = 0; j < fullGauss[i].length; j++) {
                fullGauss[i][j] = out.getMatrix(i, j);
            }
        }

        int lastColumn = 0;
        for (int i = 0; i < out.getColumn(); i++) {
            if (Double.doubleToRawLongBits(fullGauss[out.getRow() - 1][i]) != 0
                    && lastColumn == 0) {
                lastColumn = i;
            }
        }

        double temp;
        double tempFactor;
        for (int k = 0; k < lastColumn; k++) {
            for (int i = out.getRow() - 2 - k; i >= 0; i--) {
                tempFactor = fullGauss[i][lastColumn - k] / fullGauss[out.getRow() - 1 - k][lastColumn - k];
                for (int j = 0; j < out.getColumn(); j++) {
                    temp = fullGauss[out.getRow() - 1 - k][j] * tempFactor;
                    fullGauss[i][j] -= temp;
                }
            }
        }
        double newTemp = 1.0;
        if (out.getColumn() != out.getRow()) {
            for (int i = 0; i < out.getRow(); i++) {
                for (int j = 0; j < out.getColumn(); j++) {
                    if (i == j) {
                        newTemp = fullGauss[i][j];
                    }
                    fullGauss[i][j] /= newTemp;
                }
            }
        }

        out = new Matrix(fullGauss);
        if (transponent) {
            out = out.getTransponent();
        }
        return out;
    }

//    public static void main(String[] args) {
//        Matrix k = new Matrix(100, 101);
//        System.out.println("k mátrix:");
//        k.setMatrixRnd(-1.0, 1.0);
//        k.getMatrixConsole();
//        System.out.println("k mátrix gauss:");
//        k.getGauss().getMatrixConsole();
//        System.out.println("k mátrix fullgauss:");
//        k.getFullGauss().getMatrixConsole();
//        System.out.println("k mátrix Inverze:");
//        k.getInvers().getMatrixConsole();
//        System.out.println("k újra:");
//        k.getMatrixConsole();
//    }
}
