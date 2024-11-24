/* Apartment Community Management System
 * @author: Md Ahanaful Alam, Shehran Salam, Ehsan Ahmed, Shuzana Rahman
 * This program is a GUI application that allows the user to manage an apartment community.
 * It allows the user to add, search, and deliver packages, as well as manage temporary cards and lockouts.
 * It is aimed to help Office Assistants of residential communities to do complete their tasks efficiently.
 */

package src;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch the dialoguebox GUI
        SwingUtilities.invokeLater(() -> new dialoguebox().setVisible(true));
    }
}
