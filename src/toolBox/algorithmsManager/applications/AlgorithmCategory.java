package toolBox.algorithmsManager.applications;

public enum AlgorithmCategory {
	Classification(0),Cluster(1),Other(2);

    final int value;
    AlgorithmCategory(int value) {
        this.value = value;
    }

    // 把枚举值映射到整数
    public int value() {
        return value;
    }

    // 把整数映射到枚举值
    public static AlgorithmCategory valueOf(int value) {
        for(AlgorithmCategory day : AlgorithmCategory.values()) {
            if(day.value() == value) {
                return day;
            }
        }
        return null; 
    }

}
