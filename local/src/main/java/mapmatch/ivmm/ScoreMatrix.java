package mapmatch.ivmm;

import org.ujmp.core.Matrix;

import java.util.List;

public class ScoreMatrix {

    /**
     * Static Score Matrix
     */
    Matrix staticScoreMatrix(List<TimeSlice> candidateGraph) {

        int length = candidateGraph.size();

        // compute M and N for matrix
        long wholeSize = 0;
        for(TimeSlice timeSlice: candidateGraph)
            wholeSize += timeSlice.getCandidates().size();
        long firstSize = candidateGraph.get(0).getCandidates().size();
        long lastSize = candidateGraph.get(length-1).getCandidates().size();
        long M = wholeSize - lastSize, N = wholeSize - firstSize;

        // create M*N-matrix with default value
        Matrix matrix = Matrix.Factory.zeros(M, N);
        for(int i = 1; i < length; i++) {
            TimeSlice timeSlice = candidateGraph.get(i);
            // Todo: matrix.setAsDouble(value, i, j);
        }
        return matrix;
    }
}
