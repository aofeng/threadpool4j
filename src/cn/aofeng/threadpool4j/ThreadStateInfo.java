package cn.aofeng.threadpool4j;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 线程状态统计信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadStateInfo implements Serializable {

    private static final long serialVersionUID = 786834757573281082L;

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
        return new HashCodeBuilder(-667676487, -1677911895)
                .append(this.newCount).append(this.runnableCount)
                .append(this.blockedCount).append(this.waitingCount)
                .append(this.timedWaitingCount).append(this.terminatedCount)
                .toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ThreadStateInfo)) {
            return false;
        }
        ThreadStateInfo rhs = (ThreadStateInfo) object;
        return new EqualsBuilder().append(this.newCount, rhs.newCount)
                .append(this.runnableCount, rhs.runnableCount)
                .append(this.blockedCount, rhs.blockedCount)
                .append(this.waitingCount, rhs.waitingCount)
                .append(this.timedWaitingCount, rhs.timedWaitingCount)
                .append(this.terminatedCount, rhs.terminatedCount).isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("newCount", this.newCount)
                .append("runnableCount", this.runnableCount)
                .append("blockedCount", this.blockedCount)
                .append("waitingCount", this.waitingCount)
                .append("timedWaitingCount", this.timedWaitingCount)
                .append("terminatedCount", this.terminatedCount).toString();
    }

}
