package capers;

import java.io.File;
import java.io.IOException;
import java.rmi.server.UID;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        CAPERS_FOLDER.mkdirs();
        File dogs = Utils.join(CAPERS_FOLDER, "dogs");
        dogs.mkdirs();
        File story = Utils.join(CAPERS_FOLDER, "story");
        try {
            story.createNewFile();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        // TODO
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File storyfile = Utils.join(CAPERS_FOLDER, "story");
        String txt = readContentsAsString(storyfile);
        if (txt.equals("")) {
            txt = text;
        } else {
            txt = txt + "\n" + text;
        }

        writeContents(storyfile, txt);
        System.out.print(txt + "\n");
        // TODO
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        File dogfile = Utils.join(Dog.DOG_FOLDER, name);

        if (dogfile.exists()) {
            System.out.println("Dog already exists: " + name);

        } else {

            Dog newdog = new Dog(name, breed, age);
            newdog.saveDog();
            System.out.print(newdog.toString() + "\n");
        }
        // TODO
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog dog = Dog.fromFile(name);
        if (dog != null) {
            dog.haveBirthday();
            dog.saveDog();
            // 强制使用 Linux 换行符
            System.out.print(dog.toString() + "\n");
        } else {
            System.out.println("Dog not found.");
        }
        // TODO
    }
}
