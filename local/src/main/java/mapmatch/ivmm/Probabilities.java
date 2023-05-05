package mapmatch.ivmm;

/**
 * 观测概率，转移概率，空间分析函数计算公式
 */
class Probabilities {
    /**
     * 正态分布参数
     */
    private double sigma = 20;

    /**
     * 构造函数
     */
    Probabilities() {
    }
    Probabilities(double sigma) {
        this.sigma = sigma;
    }

    /**
     * 候选点观测概率
     */
    double candidatePointLogProbability(double distance) {
        return logNormalDistribution(this.sigma, distance);
    }

    /**
     * 转移概率
     */
    double transitionLogProbability(double routeLength, double linearDistance) {
        return Math.abs(linearDistance - routeLength);
    }

    /**
     * 空间分析函数
     */
    double analysisFunction(double distance, double routeLength, double linearDistance) {
        return candidatePointLogProbability(distance) * transitionLogProbability(routeLength, linearDistance);
    }

    /**
     * 取对数后的正态分布公式
     * @param sigma 正态分布参数
     * @param x 距离
     * @return 概率
     */
    private static double logNormalDistribution(double sigma, double x) {
        return Math.log(1.0 / (Math.sqrt(2.0 * Math.PI) * sigma)) + (-0.5 * Math.pow(x / sigma, 2));
    }
}

