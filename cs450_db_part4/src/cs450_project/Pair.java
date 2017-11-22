/**
 * 
 */
package cs450_project;

import java.math.BigDecimal;

/**
 * @author ivan
 * A class of Pairs, this is a custom class where each pair specifically is used in manner
 * of (Project, Hours)
 */
public class Pair {
		private String project;
		private BigDecimal pno;
		private double hours;
		
		public Pair() {
			
		}
		
		public Pair(String pName, double pHours) {
			this.project = pName;
			this.hours = pHours;
		}
		
		public String getProject() {
			return this.project;
		}
		
		public BigDecimal getProjectPno() {
			return this.pno;
		}
		
		public double getHours() {
			return this.hours;
		}
		
		public void setProject(String p) {
			this.project = p;
		}
		
		public void setProjectPno(BigDecimal p) {
			this.pno = p;
		}
		
		public void setHours(double parseDouble ) {
			this.hours = parseDouble;
		}
		

}
