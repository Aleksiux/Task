import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class Main {


    //About the task.
    //Task is learn to create a program that opens, deletes, copy's files and etc.





        public static void main(String [] args) throws FileNotFoundException {
            Path currentDirectory  = FileSystems.getDefault().getPath("").toAbsolutePath();
            Scanner scanner = new Scanner(System.in);

            String oldDir = System.setProperty("user.dir", String.valueOf(currentDirectory));


            for (; ;) {
                System.out.println("Write any command you want:\n mvp (show's current working directory,\n list (show's what is in the folder),\n del (deletes folder),\n crt (creates folder),\n copy (copy file). ");
                String scanneri = scanner.nextLine();
                try {
                    mvpCurrentDirectory(currentDirectory, scanneri);
                    listOfDirectoryOfFiles(currentDirectory, scanneri);
                    enterDirectory(scanneri);
                    copyFile(scanneri);
                    ChangeCurrentLocation(currentDirectory, scanner, scanneri);
                    CreateDirectory(scanneri, scanner);
                    deleteFromCurrentDirectory(scanner, scanneri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(scanneri.equals("stop")){
                    System.out.println("See you later...");
                    break;
                }
            }
        }

    private static void CreateDirectory(String scanneri, Scanner scanner) throws IOException {
        if (scanneri.equals("crt")) {
            System.out.println("Enter file name");
            String fileName = scanner.next();
            Path path = Paths.get(fileName);
            Files.createDirectories(path);
        }
    }


    private static void ChangeCurrentLocation(Path currentDirectory, Scanner scanner, String scanneri) {
        if (scanneri.equals("cd")) {
            System.out.println("Please enter destination");
            String cdScanner = scanner.nextLine();
            System.setProperty("user.dir", cdScanner);
            System.out.println("The current working directory is " + currentDirectory);
        }
    }

    private static void deleteFromCurrentDirectory(Scanner scanner, String scanneri) throws IOException {
        if (scanneri.equals("del")) {
            System.out.println("Enter the path to delete a directory");
            String delFolder = scanner.next();
            Path path = Paths.get(delFolder);
            Files.delete(path);
        }
    }

    private static void enterDirectory(String scanneri) throws FileNotFoundException {
        if (scanneri.equals("enter")){
            File myFile = new File(System.getProperty("user.dir"), "localpath.ext");
            InputStream openit = new FileInputStream(myFile);
        }
    }

    private static void listOfDirectoryOfFiles(Path currentDirectory, String scanneri) throws IOException {
        if (scanneri.equals("list")) {
            Files.walkFileTree(currentDirectory, EnumSet.noneOf(FileVisitOption.class), 1, new PrintFiles());
        }
    }

    private static void mvpCurrentDirectory(Path currentDirectory, String scanneri) {
        if (scanneri.equals("mvp")) {
            System.out.println("The current working directory is " + currentDirectory);
        }
    }

    private static void copyFile(String scanneri) throws IOException {
            if(scanneri.equals("copy")) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Provide source file name :");
                String sfile = sc.next();
                System.out.print("Provide destination file name :");
                String dfile = sc.next();
                FileReader fin = new FileReader(sfile);
                FileWriter fout = new FileWriter(dfile, true);
                int c;
                while ((c = fin.read()) != -1) {
                    fout.write(c);
                }
                System.out.println("Copy finish...");
                fin.close();
                fout.close();
            }
    }

    private static class PrintFiles extends SimpleFileVisitor<Path> {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                if (attr.isDirectory()) {
                    try {
                        System.out.format("Directory: %s, size: %d bytes\n", file, getDirSize(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (attr.isRegularFile()) {
                    System.out.format("Regular file: %s, size %d bytes\n", file, attr.size());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println(exc);
                return FileVisitResult.CONTINUE;
            }

            private long getDirSize(Path dirPath) throws IOException {
                final AtomicLong size = new AtomicLong(0L);

                Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        size.addAndGet(attrs.size());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });

                return size.get();
            }
        }


    }