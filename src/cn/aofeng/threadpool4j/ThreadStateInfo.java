package cn.aofeng.threadpool4j;

import java.io.Serializable;

/**
 * 线程状态统计信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadStateInfo implements Serializable {

    private static final long serialVersionUID = 5759858852685030129L;

    int newCount;

    int runnableCount;

    int blockedCount;

    int waitingCount;

    int timedWaitingCount;

    int terminatedCount;

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public int getRunnableCount() {
        return runnableCount;
    }

    public void setRunnableCount(int runnableCount) {
        this.runnableCount = runnableCount;
    }

    public int getBlockedCount() {
        return blockedCount;
    }

    public void setBlockedCount(int blockedCount) {
        this.blockedCount = blockedCount;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }

    public int getTimedWaitingCount() {
        return timedWaitingCount;
    }

    public void setTimedWaitingCount(int timedWaitingCount) {
        this.timedWaitingCount = timedWaitingCount;
    }

    public int getTerminatedCount() {
        return terminatedCount;
    }

    public void setTerminatedCount(int terminatedCount) {
        this.terminatedCount = terminatedCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + blockedCount;
        result = prime * result + newCount;
        result = prime * result + runnableCount;
        result = prime * result + terminatedCount;
        result = prime * result + timedWaitingCount;
        result = prime * result + waitingCount;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadStateInfo)) {
            return false;
        }
        ThreadStateInfo other = (ThreadStateInfo) obj;
        if (blockedCount != other.blockedCount) {
            return false;
        }
        if (newCount != other.newCount) {
            return false;
        }
        if (runnableCount != other.runnableCount) {
            return false;
        }
        if (terminatedCount != other.terminatedCount) {
            return false;
        }
        if (timedWaitingCount != other.timedWaitingCount) {
            return false;
        }
        if (waitingCount != other.waitingCount) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(256)
            .append("ThreadStateInfo [newCount=").append(newCount)
            .append(", runnableCount=").append(runnableCount)
            .append(", blockedCount=").append(blockedCount)
            .append(", waitingCount=").append(waitingCount)
            .append(", timedWaitingCount=").append(timedWaitingCount)
            .append(", terminatedCount=").append(terminatedCount)
            .append("]");
        return buffer.toString();
    }

}
