package com.n26.transaction.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionStatistics {

	private BigDecimal sum;

	private BigDecimal avg;

	private BigDecimal max;

	private BigDecimal min;

	private long count;

	public TransactionStatistics() {
	}

	public TransactionStatistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count) {
		super();
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	@JsonIgnore
	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	@JsonIgnore
	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	@JsonIgnore
	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	@JsonIgnore
	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@JsonProperty("sum")
	public String getSumString() {
		return sum.toString();
	}

	@JsonProperty("avg")
	public String getAvgString() {
		return avg.toString();
	}

	@JsonProperty("max")
	public String getMaxString() {
		return max.toString();
	}

	@JsonProperty("min")
	public String getMinString() {
		return min.toString();
	}

	public TransactionStatistics setBigDecimalScale() {
		sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
		avg = avg.setScale(2, BigDecimal.ROUND_HALF_UP);
		max = max.setScale(2, BigDecimal.ROUND_HALF_UP);
		min = min.setScale(2, BigDecimal.ROUND_HALF_UP);
		return this;
	}

	@Override
	public String toString() {
		return "[sum=" + sum + ", avg=" + avg + ", max=" + max + ", min=" + min + ", count=" + count + "]";
	}
}
