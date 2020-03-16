package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

public class SummaryController {

    @FXML
    public Label employeeSummaryfield;

    @FXML
    public Label employeeSummaryLabel;

    Map<Integer, String> employeeSummary = new HashMap<>();

    public  void initialize(){
        fetchEmployeeSummary();
    }

    private void fetchEmployeeSummary() {
        employeeSummary.put(1,"");
        employeeSummary.put(2,"");
        employeeSummary.put(3,"");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true){
//                   employeeSummary.put(count%employeeSummary.size(),MainRepository.getInstance().getSummary(count%employeeSummary.size()));
                    int finalCount = count;
                    String value = MainRepository.getInstance().getSummary(finalCount %employeeSummary.size());
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            employeeSummaryfield.setText(value);
                            employeeSummaryLabel.setText(getState(finalCount %employeeSummary.size()));
                        }
                    });

                    count++;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public String getState(int action){
        switch (action){
            case 0:
                return "Total Employees";
            case 1:
                return "HIGH SALARY";
            case 2:
            default:
                return "LOW SALARY";
        }
    }

}
