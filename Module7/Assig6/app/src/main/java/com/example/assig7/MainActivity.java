package com.example.assig7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    EditText etMain;
    Button btnSubmitMain;
    TextView tvMain, tvCourse;

    /**
     * App runs only on onCreate instance of Android App Lifecycle.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link objects to entry in resources
        etMain = findViewById(R.id.etMain);
        btnSubmitMain = findViewById(R.id.btnSubmitMain);
        tvMain = findViewById(R.id.tvMain);
        tvCourse = findViewById(R.id.tvCourse);

        //hide initial course description text view & display course list
        tvMain.setText(setTextViewCourseList());
        tvCourse.setVisibility(View.INVISIBLE);

        /**
         * Assigns actions to the "Submit" button
         */
        btnSubmitMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //assigns input from edit text field to userInput, trimming white space
                String userInput = etMain.getText().toString().trim();

                //performs input validation and performs course lookup if true
                if(inputValidation(userInput))
                {
                    int courseID = Integer.parseInt(userInput);
                    setTvCourse(courseID);
                    tvCourse.setVisibility(View.VISIBLE);
                }
                else //warning message is displayed if input validation fails
                {
                    errorDialog();
                }
            }
        });

    }

    /**
     * Identifies if String contains non-digits
     * @param userInput String object to be validated
     * @return TRUE if only digits present, FALSE if letters or symbols are present
     */
    private boolean inputValidation(String userInput)
    {
        return userInput.matches("^[0-9]+$");
    }

    /**
     * Sets description of course in tvCourse TextView if there is a match.
     * @param courseNumber integer value to be matched to a course description
     */
    private void setTvCourse(int courseNumber)
    {
        switch(courseNumber){
            case 300:
                tvCourse.setText(getCST300());
            case 311:
                tvCourse.setText(getCST311());
                break;
            case 334:
                tvCourse.setText(getCST334());
                break;
            case 336:
                tvCourse.setText(getCST336());
                break;
            case 338:
                tvCourse.setText(getCST338());
                break;
            case 363:
                tvCourse.setText(getCST363());
                break;
            case 370:
                tvCourse.setText(getCST370());
                break;
            case 438:
                tvCourse.setText(getCST438());
                break;
            case 462:
                tvCourse.setText(getCST462());
                break;
            case 499:
                tvCourse.setText(getCST499());
                break;
            case 383:
                tvCourse.setText(getCST383());
                break;
            case 325:
                tvCourse.setText(getCST325());
                break;
            default:
                errorDialog();
        }

    }

    /**
     * Formatted output for Course List
     * @return String representing course list
     */
    private String setTextViewCourseList()
    {
        return "CST 300 - Major ProSeminar\n" +
                "CST 311 - Intro to Computer Networks\n" +
                "CST 334 - Operating Systems\n" +
                "CST 336 - Internet Programming\n" +
                "CST 338 - Software Design\n" +
                "CST 363 - Introduction to Database Systems\n" +
                "CST 370 - Design and Analysis of Algorithms\n" +
                "CST 438 - Software Engineering\n" +
                "CST 462S - Race, Gender, Class in the Digital World\n" +
                "CST 499 - Computer Science Capstone\n" +
                "CST 383 - Introduction to Data Science\n" +
                "CST 325 - Graphics Programming";
    }

    /**
     * Formatted output for CST 300
     * @return String object representing CST 300 course description
     */
    private String getCST300()
    {
        return "\n" +
                "CST 300 - Major ProSeminar\n" +
                "Helps students identify and articulate personal, professional, and social " +
                "goals. Provides an integrated overview of the computer science and " +
                "communication design majors and their requirements. Students develop a plan " +
                "for their learning goals. Students learn writing, presentation, research and " +
                "critical-thinking skills within the diversified fields of information " +
                "technology and communication design. Students learn how to analyze, predict, " +
                "and articulate trends in the academic, public service,\n";
    }

    /**
     * Formatted output for CST 338
     * @return String object representing CST 338 course description
     */
    private String getCST338()
    {
        return "CST 338 - Software Design\n" +
                "Provides students with the fundamental concepts to develop large-scale " +
                "software, focusing on the object-oriented programming techniques. Coverage " +
                "includes the introduction to Java programming language, object-oriented " +
                "programming, software life cycle and development processes, requirements " +
                "analysis, and graphical user interface development.\n";
    }

    /**
     * Formatted output for CST 311
     * @return String object representing CST 311 course description
     */
    private String getCST311()
    {
        return "CST 311 - Intro to Computer Networks\n" +
                "Survey of Telecomm and Data Comm Technology Fundamentals, Local Area Network," +
                " Wide Area Network, Internet and internetworking protocols including TCP/IP," +
                " network security and performance, emerging industry trends such as voice" +
                " over the network and high speed networking. Designed as a foundation for" +
                " students who wish to pursue more advanced network studies including" +
                " certificate programs. Includes hands-on networking labs that incorporate" +
                " Cisco CCNA lab components.\n";
    }

    /**
     * Formatted output for CST 334
     * @return String object representing CST 334 course description
     */
    private String getCST334()
    {
        return "CST 334 - Operating Systems\n" +
                "Students in this course will learn about the use and design of modern" +
                " operating systems, focusing on Linux. On the “use” side, students will" +
                " learn the Linux command line, to write shell scripts, and to build programs" +
                " with GNU utilities like awk, sed, and make. On the “design” side, students" +
                " will develop a deep understanding of process management, memory management," +
                " file systems, and concurrency, and how they apply to modern technologies like" +
                " virtualization and cloud computing.\n";
    }

    /**
     * Formatted output for CST 336
     * @return String object representing CST 336 course description
     */
    private String getCST336()
    {
        return "CST 336 - Internet Programming\n" +
                "Provides students with dynamic web application development skills, focusing" +
                " on the integration of server-side programming, database connectivity, and" +
                " client-side scripting. Coverage includes the Internet architecture, responsive" +
                " design, RESTful web services, and Web APIs. \n";
    }

    /**
     * Formatted output for CST 363
     * @return String object representing CST 363 course description
     */
    private String getCST363()
    {
        return "CST 363 - Introduction to Database Systems\n" +
                "This course provides balanced coverage of database use and design,focusing" +
                " on relational databases. Students will learn to design relational schemas," +
                " write SQL queries, access a DB programmatically,and perform database" +
                " administration. Students will gain a working knowledge of the algorithms" +
                " and data structures used in query evaluation and transaction processing." +
                " Students will also learn to apply newer database technologies such as XML," +
                " NoSQL, and Hadoop.\n";
    }

    /**
     * Formatted output for CST 370
     * @return String object representing CST 370 course description
     */
    private String getCST370()
    {
        return "CST 370 - Design and Analysis of Algorithms\n" +
                "Students learn important data structures in computer science and acquire" +
                " fundamental algorithm design techniques to get the efficient solutions to" +
                " several computing problems from various disciplines. Topics include the " +
                "analysis of algorithm efficiency, hash, heap, graph, tree, sorting and " +
                "searching, brute force, divide-and-conquer, decrease-and-conquer, " +
                "transform-and-conquer, dynamic programming, and greedy programming.\n";
    }

    /**
     * Formatted output for CST 438
     * @return String object representing CST 438 course description
     */
    private String getCST438()
    {
        return "CST 438 - Software Engineering\n" +
                "Prepares students for large-scale software development using software " +
                "engineering principles and techniques. Coverage includes software process, " +
                "requirements analysis and specification, software design, implementation, " +
                "testing, and project management. Students are expected to work in teams to " +
                "carry out a realistic software project.\n";
    }

    /**
     * Formatted output for CST 462
     * @return String object representing CST 462 course description
     */
    private String getCST462()
    {
        return "CST 462S - Race, Gender, Class in the Digital World\n" +
                "Provides students with key knowledge of race, gender, class and social justice" +
                " especially in relation to technology in today’s digital world. Students " +
                "challenge the barriers of expertise, gender, race, class, and location that " +
                "restrict wider access to and understanding of the production and usage of new " +
                "technologies. Students will engage in a practical experience in the community " +
                "via their service placements, which will provide depth and context for " +
                "considering questions of justice, equality, social responsibilities and the" +
                " complexities of technology and its societal impact. The course uses scenario" +
                " based approach combining presentations, discussions, and reflections to allow " +
                "students explore the relationship between critical reflection and action on the " +
                "topics mentioned above. \n";
    }

    /**
     * Formatted output for CST 383
     * @return String object representing CST 383 course description
     */
    private String getCST383()
    {
        return "CST 383 - Introduction to Data Science\n" +
                "In data science, data analysis and machine learning techniques are applied" +
                " to visualize data, understand trends, and make predictions. In this course" +
                " students will learn how to obtain data, preprocess it, apply machine learning" +
                " methods, and visualize the results. A student who completes the course will" +
                " have enough theoretical knowledge, and enough skill with modern statistical" +
                " programming languages and their libraries,to define and perform complete" +
                " data science projects.\n";
    }

    /**
     * Formatted output for CST 325
     * @return String object representing CST 325 course description
     */
    private String getCST325()
    {
        return "CST 325 - Graphics Programming\n" +
                "This course teaches the students the fundamentals of game programming" +
                " and skills needed for game development, including GPU programming, matrix" +
                " and quaternion algebra for physics calculation, animation, lighting and " +
                "basics of implementing 3D models into a framework.\n";
    }

    /**
     * Formatted output for CST 499
     * @return String object representing CST 499 course description
     */
    private String getCST499()
    {
        return"CST 499 - Computer Science Capstone\n" +
                "Students will work on a project in large groups (up to 5 students in" +
                " each group), developing requirements specification, a solution plan" +
                " followed by design and implementation of the solution. The problem " +
                "statement for the projects will be selected by the faculty. Faculty " +
                "will also play the role of a project manager directing the schedule and" +
                " deliverables for these projects.\n";
    }

    /**
     * Displays pop up error message.
     */
    private void errorDialog()
    {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);
        dialogueBuilder.setTitle("Input Error");
        dialogueBuilder.setMessage("Input error: Please enter a valid 3 digit course number.");

        //error message only displays one button that allows for confirmation/dismissal
        dialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogueBuilder.show();
    }
}