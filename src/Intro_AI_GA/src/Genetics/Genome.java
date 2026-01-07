package Genetics;

import Model.Course;
import Model.Room;
import Model.Timeslot;

/**
 * A representation of a course scheduled to a timeslot and room. Aka gene
 */
public class Genome {

	public Room rm;
	public Timeslot ts;
	public Course course;


	public Genome(Room rm, Course course, Timeslot ts) {
		this.rm = rm;
		this.course = course;
		this.ts = ts;
	}

	@Override
	public Genome clone() {
		return new Genome(rm, course, ts);
	}
}
