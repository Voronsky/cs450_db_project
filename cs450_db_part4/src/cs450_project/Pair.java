/**
 * 
 */
package cs450_project;

/**
 * @author ivan
 *
 */
public class Pair {
		private String project;
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
		
		public double getHours() {
			return this.hours;
		}
		
		public void setProject(String p) {
			this.project = p;
		}
		
		public void setHours(double parseDouble ) {
			this.hours = parseDouble;
		}
		

}
