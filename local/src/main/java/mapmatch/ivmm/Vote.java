package mapmatch.ivmm;

import org.apache.spark.model.st.STPoint;
import scala.Tuple2;
import point.CandidatePoint;
import mapmatch.tihmm.SequenceState;

import java.util.*;

public class Vote {

    /**
     * 上一个状态下原始轨迹点对应的所有candidate point
     */
    private List<CandidatePoint> prevCandidates;
    /**
     * 每个candidate point 对应的p
     */
    public Map<CandidatePoint, Double> message;



    /**
     * 初始状态概率函数
     *
     * @param observation             原始轨迹点
     * @param candidates              原始轨迹点对应的candidates
     * @param initialLogProbabilities 初始状态概率
     */
    private void initializeStateProbabilities(STPoint observation, List<CandidatePoint> candidates, Map<CandidatePoint, Double> initialLogProbabilities) {
        if (message != null) {
            throw new IllegalArgumentException("Initial probabilities have already been set.");
        }
        Map<CandidatePoint, Double> initialMessage = new HashMap<>(initialLogProbabilities.size());
        for (CandidatePoint candidate : candidates) {
            if (!initialLogProbabilities.containsKey(candidate)) {
                throw new IllegalArgumentException("No initial probability for " + candidate);
            }
            double logProbability = initialLogProbabilities.get(candidate);
            initialMessage.put(candidate, logProbability);
        }
        message = initialMessage;

    }

    /**
     * 如果初始化的概率有接近武无穷小的，停止初始化
     *
     * @param message 状态概率
     * @return
     */
    private Boolean hmmBreak(Map<CandidatePoint, Double> message) {
        for (Double logProbability : message.values()) {
            if (!logProbability.equals(Double.NEGATIVE_INFINITY)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 给定transition 计算对应概率
     *
     * @param preState                   之前的state
     * @param curState                   现在的state
     * @param transitionLogProbabilities 转移概率的map
     * @return 概率p
     */
    private double transitionLogProbability(CandidatePoint preState, CandidatePoint curState, Map<Tuple2<CandidatePoint, CandidatePoint>, Double> transitionLogProbabilities) {
        Tuple2<CandidatePoint, CandidatePoint> transition = new Tuple2<>(preState, curState);
        return transitionLogProbabilities.getOrDefault(transition, Double.NEGATIVE_INFINITY);
    }

    /**
     * 计算当前状态下概率最大对应的state
     *
     * @return candidate point
     */
    private CandidatePoint mostLikelyState() {
        assert message.size() != 0;
        CandidatePoint result = null;
        Double maxLogProbability = Double.NEGATIVE_INFINITY;
        for (Map.Entry<CandidatePoint, Double> entry : message.entrySet()) {
            if (entry.getValue() > maxLogProbability) {
                result = entry.getKey();
                maxLogProbability = entry.getValue();
            }
        }
        assert result != null;
        return result;
    }

    /**
     * 取回概率最大的一系列转移过程
     *
     * @return list，包含每一步转移对应的状态
     */
    private List<SequenceState> retrieveMostLikelySequence() {
        assert message.size() != 0;
        CandidatePoint lastState = mostLikelyState();
        List<SequenceState> result = new ArrayList<>();
        Collections.reverse(result);
        return result;
    }

    /**
     * @param observation              原始轨迹点
     * @param candidates               对应的candidates
     * @param emissionLogProbabilities 每一个candidate对应的 emission p
     */
    void startWithInitialObservation(STPoint observation, List<CandidatePoint> candidates, Map<CandidatePoint, Double> emissionLogProbabilities) {
        initializeStateProbabilities(observation, candidates, emissionLogProbabilities);
    }

}
