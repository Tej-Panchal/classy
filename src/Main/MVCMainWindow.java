package Main;

import DataBase.DataBase;

public class MVCMainWindow {
    
    public static void main(String[] args) {  	
       	DataBase database = DataBase.getDB();
       	
    	MainWindowView mainWindowView = new MainWindowView();
    	
		MainWindowController theController = new MainWindowController(mainWindowView);
        
        mainWindowView.setVisible(true);
    }
}