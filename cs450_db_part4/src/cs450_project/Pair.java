/**
 * 
 */
package cs450_project;

/**
 * @author ivan
 * A class of Pairs, this is a custom class where each pair specifically is used in manner
 * of (Project, Hours)
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
