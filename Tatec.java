import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;




public class Tatec
{
    private static final int CORRECT_TOTAL_TOKEN_PER_STUDENT = 100;
    private static final String OUT_TATEC_UNHAPPY = "unhappyOutTATEC.txt";
    private static final String OUT_TATEC_ADMISSION = "admissionOutTATEC.txt";
    private static final String OUT_RAND_UNHAPPY = "unhappyOutRANDOM.txt";
    private static final String OUT_RAND_ADMISSION = "admissionOutRANDOM.txt";

    public static void main(String args[])
    {



        if(args.length < 4)
        {
            System.err.println("Not enough arguments!");
            return;
        }

        // File Paths
        String courseFilePath = args[0];
        String studentIdFilePath = args[1];
        String tokenFilePath = args[2];
        double h;

        try { h = Double.parseDouble(args[3]);}
        catch (NumberFormatException ex)
        {
            System.err.println("4th argument is not a double!");
            return;
        }

        // TODO: Rest is up to you

        AtomicInteger num_courses = new AtomicInteger();
        try (Stream<String> stream = Files.lines(Paths.get(courseFilePath))) {

            stream.forEach(y -> num_courses.getAndIncrement());

        } catch (IOException e) {
            e.printStackTrace();
        }



        AtomicInteger num_students = new AtomicInteger();
        ArrayList<String> studentList = new ArrayList<String>();


        try (Stream<String> stream = Files.lines(Paths.get(studentIdFilePath))) {

            stream.forEach(y -> {
                num_students.getAndIncrement();
                studentList.add(y);

            });

        } catch (IOException e) {
            e.printStackTrace();
        }




        //split elements of test_studentToken and store as list of an ArrayList without using for loop
        /*ArrayList<ArrayList<String>> studentToken = new ArrayList<ArrayList<String>>();
        try (Stream<String> stream = Files.lines(Paths.get(tokenFilePath))) {
            stream.forEach(y -> {
                studentToken.add(new ArrayList<String>(Arrays.asList(y.split(","))));

            });


        } catch (IOException e) {
            e.printStackTrace();

        } */

        List<List<Integer>> studentToken = new ArrayList<>();
        List<Integer> num_of_non_zero_tokens = new ArrayList<>();


        try {
            Files.lines(Paths.get(tokenFilePath)).forEach(y -> {
                List <String> str = (Arrays.asList(y.split(",")));
                studentToken.add(str.stream().map(Integer::parseInt).collect(Collectors.toList()));
                AtomicInteger count = new AtomicInteger();
                str.stream().map(Integer::parseInt).collect(Collectors.toList()).forEach(x -> {
                    if(x != 0)
                    {
                        count.getAndIncrement();
                    }

                });
                num_of_non_zero_tokens.add(count.get());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //log num_of_non_zero_tokens





        ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>();
        try (Stream<String> stream = Files.lines(Paths.get(courseFilePath))) {

            stream.forEach(y -> courses.add(new ArrayList<String>(Arrays.asList(y.split(",")))));

        } catch (IOException e) {
            e.printStackTrace();

        }
        //log courses


        //delete file if it exists
        try {
            Files.deleteIfExists(Paths.get(OUT_TATEC_UNHAPPY));
            Files.deleteIfExists(Paths.get(OUT_TATEC_ADMISSION));
            Files.deleteIfExists(Paths.get(OUT_RAND_UNHAPPY));
            Files.deleteIfExists(Paths.get(OUT_RAND_ADMISSION));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File myObj = new File(OUT_TATEC_ADMISSION);
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
















        //admissionOutTATEC.txt

        AtomicInteger index_course = new AtomicInteger(0);
        courses.stream().forEach(y -> {
            //write first element of y to file
            try {
                //Files.write(Paths.get(OUT_TATEC_UNHAPPY), (y.get(0) + "\n").getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(OUT_TATEC_ADMISSION), (y.get(0) + ", ").getBytes(), StandardOpenOption.APPEND);
                //get index of y


                int index = index_course.intValue();

                ArrayList<Integer> course_demand_1 = new ArrayList<Integer>();
                studentToken.stream().forEach(x -> course_demand_1.add(Integer.valueOf(x.get(index))));

                int capacity;
                capacity = Integer.parseInt(courses.get(index).get(1));


                course_demand_1.sort((a, b) -> b - a);


                int limit = course_demand_1.get(capacity - 1);


                ArrayList<Integer> student_admitted = new ArrayList<Integer>();
                AtomicInteger index_z = new AtomicInteger(0);
                studentToken.stream().forEach(z -> {

                    if (z.get(index) >= limit && z.get(index) != 0) {
                        //find index of y
                        int index1 = index_z.intValue();
                        student_admitted.add(index1);
                    }
                    index_z.getAndIncrement();
                });

                student_admitted.stream().forEach(t -> {
                    String s = studentList.get(t);
                    //write s to file without deleting previous content
                    try {
                        Files.write(Paths.get(OUT_TATEC_ADMISSION), s.getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(OUT_TATEC_ADMISSION), ", ".getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                Files.write(Paths.get(OUT_TATEC_ADMISSION), ("\n").getBytes(), StandardOpenOption.APPEND);

                index_course.getAndIncrement();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });








        //unhappyOutTATEC.txt


        File myObj1 = new File(OUT_TATEC_UNHAPPY);
        try {
            myObj1.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AtomicReference<Double> avg_u = new AtomicReference<>((double) 0);
        studentList.stream().forEach(y -> {

            int index = studentList.indexOf(y);




            AtomicReference<Double> U = new AtomicReference<>((double) 0);
            AtomicInteger index_x1 = new AtomicInteger(0);
            studentToken.get(index).stream().forEach(x -> {
                if(x != 0){
                    int index_column = index_x1.intValue();
                    //search for studentId in admissionOutTATEC.txt index_column'th row and if find it then print it to unhappyOutTATEC.txt without for loop
                    try {
                        List<String> lines = Files.readAllLines(Paths.get(OUT_TATEC_ADMISSION));
                        String line = lines.get(index_column);
                        if(line.contains(studentList.get(index))){

                        }
                        else{
                            //define U variable for unhappines and increase by t value for every course that student is not admitted

                            double t = (-100 / h)*(Math.log(1 - (x / 100.0)));
                            if(t > 100){
                                t = 100;
                            }
                            U.set(U.get() + t);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                index_x1.getAndIncrement();
            });

            //write U to unhappyOutTATEC.txt
            try {
                Files.write(Paths.get(OUT_TATEC_UNHAPPY), ("" + U.get() ).getBytes(), StandardOpenOption.APPEND);
                avg_u.set(avg_u.get() + U.get());
            } catch (IOException e) {
                e.printStackTrace();
            }






            try {
                Files.write(Paths.get(OUT_TATEC_UNHAPPY), "\n".getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }



        });

        avg_u.set(avg_u.get() / studentList.size());
        //write avg_u to the top of the unhappyOutTATEC.txt file

        try {
            List<String> lines = Files.readAllLines(Paths.get(OUT_TATEC_UNHAPPY));
            lines.add(0, "" + avg_u.get());
            Files.write(Paths.get(OUT_TATEC_UNHAPPY), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }










        //OUT_RAND_ADMISSION
        File myObj2 = new File(OUT_RAND_ADMISSION);
        try {
            myObj2.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        AtomicInteger index_course_rand = new AtomicInteger(0);
        courses.stream().forEach(y -> {
            try {
                Files.write(Paths.get(OUT_RAND_ADMISSION), (y.get(0) + ", ").getBytes(), StandardOpenOption.APPEND);
                int index = index_course_rand.intValue();


                final int[] capacity = new int[1];
                capacity[0] = Integer.parseInt(courses.get(index).get(1));


                studentList.stream().forEach(x -> {
                    if (capacity[0] > 0) {
                        int index_student = studentList.indexOf(x);
                        if (num_of_non_zero_tokens.get(index_student) > 0) {
                            try {
                                Files.write(Paths.get(OUT_RAND_ADMISSION), (x + ", ").getBytes(), StandardOpenOption.APPEND);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            capacity[0]--;
                            num_of_non_zero_tokens.set(index_student, num_of_non_zero_tokens.get(index_student) - 1);
                        }
                    }
                });





                try {
                    Files.write(Paths.get(OUT_RAND_ADMISSION), ("\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }




            } catch (IOException e) {
                e.printStackTrace();
            }
        });





        //OUT_RAND_UNHAPPY



        File myObj4 = new File(OUT_RAND_UNHAPPY);
        try {
            myObj4.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AtomicReference<Double> avg_u_rand = new AtomicReference<>((double) 0);
        studentList.stream().forEach(y -> {

            int index = studentList.indexOf(y);




            AtomicReference<Double> U = new AtomicReference<>((double) 0);
            AtomicInteger index_x1 = new AtomicInteger(0);
            studentToken.get(index).stream().forEach(x -> {
                if(x != 0){
                    int index_column = index_x1.intValue();
                    //search for studentId in admissionOutTATEC.txt index_column'th row and if find it then print it to unhappyOutTATEC.txt without for loop
                    try {
                        List<String> lines = Files.readAllLines(Paths.get(OUT_RAND_ADMISSION));
                        String line = lines.get(index_column);
                        if(line.contains(studentList.get(index))){

                        }
                        else{
                            //define U variable for unhappines and increase by t value for every course that student is not admitted

                            double t = (-100 / h)*(Math.log(1 - (x / 100.0)));
                            if(t > 100){
                                t = 100;
                            }
                            U.set(U.get() + t);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                index_x1.getAndIncrement();
            });


            //write U to unhappyOutTATEC.txt
            try {
                Files.write(Paths.get(OUT_RAND_UNHAPPY), ("" + U.get() ).getBytes(), StandardOpenOption.APPEND);
                avg_u_rand .set(avg_u_rand .get() + U.get());
            } catch (IOException e) {
                e.printStackTrace();
            }






            try {
                Files.write(Paths.get(OUT_RAND_UNHAPPY), "\n".getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }



        });

        avg_u_rand.set(avg_u_rand .get() / studentList.size());

        try {
            List<String> lines = Files.readAllLines(Paths.get(OUT_RAND_UNHAPPY));
            lines.add(0, "" + avg_u_rand.get());
            Files.write(Paths.get(OUT_RAND_UNHAPPY), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }










    }

}
