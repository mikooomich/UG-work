//Kelvin's work

/**
 * This class is used to create People objects
 * 
 * @class
 */
class People {

  /**
   * The constructor for People objects
   * @constructor
   * @param {string} job - The name the position/job
   * @param {number} income - The income of a person working at the job
   * @param {string} experience - A description of what is required to work at the job/ credentials
   * @param {string} company - The company the person works at
   * @param {string} firstName - The first name of the person
   * @param {string} lastName - The last name of the person
   * @param {number} yearsOfWork - The number of years the person worked at the company
   * @param {number} age - The age of the person
   * @param {string} ethnicity - The ethnicity of the person
   * @param {string} religion - The religious affliation of the person
   * @param {string} gender - The gender identity of the person
   * @param {string} sOrientation - The sexual orientation of the person
   */
    constructor(job, income, experience, company, firstName, lastName, yearsOfWork, age, ethnicity, religion, gender, sOrientation){
      this.job = job;
      this.income = income;
      this.experience = experience;
      this.company = company;
      this.firstName = firstName;
      this.lastName = lastName;
      this.yearsOfWork = yearsOfWork;
      this.age = age;
      this.ethnicity = ethnicity;
      this.religion = religion;
      this.gender = gender;
      this.sOrientation = sOrientation;
    };
   
};
  