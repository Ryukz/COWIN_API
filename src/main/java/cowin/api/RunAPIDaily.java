package cowin.api;

import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class RunAPIDaily {
     static Response response;
     static ArrayList<Object> ageLimitArray =new ArrayList<>();
     static ArrayList<Object> availableCapacity =new ArrayList<>();
     static ArrayList<Integer> slotsOpenIndex=new ArrayList<>();
     static ArrayList<String> centerName=new ArrayList<>();
    static ArrayList<String> centerAddress=new ArrayList<>();
    static ArrayList<String> vaccineName=new ArrayList<>();
    static ArrayList<String> capacity=new ArrayList<>();
    @Scheduled(fixedRateString = "60000")  //Check Slot Every 1 min
    public void RunCowinAPI() throws IOException, InterruptedException {
        RunAPI runAPI=new RunAPI();
        try {
            response=runAPI.getSlot();
            Assert.assertEquals(response.getStatusCode(),200,"Code is 200");
        }
        catch (AssertionError assertionError)
        {
            System.out.println("Retrying to Run API Again!!!");
            Thread.sleep(2000);
            runAPI.getSlot();
        }

        ageLimitArray =response.path("centers.sessions.min_age_limit");
         response=runAPI.getSlot();
         ageLimitArray=response.path("centers.sessions.min_age_limit");
         availableCapacity=response.path("centers.sessions.available_capacity");
        for(int i = 0; i< ageLimitArray.size(); i++)
         {
             if(ageLimitArray.get(i).toString().contains("45") || ageLimitArray.get(i).toString().contains("18"))    //Checking Slots with min_age_limit equals to 18yrs
             {
                 if(!availableCapacity.get(i).toString().contains("0"))
                 {
                     slotsOpenIndex.add(i);
                 }
             }

         }
        if(slotsOpenIndex.size()==0)
            System.out.println("No Slot Open!!");
        else
        {
            System.out.println("Slots Available!!!!");
            System.out.println("Sending Mail!!!!");
            JSONObject centersDetails=new JSONObject();
            for(int i=0;i<slotsOpenIndex.size();i++)
            {   centerName.add(response.path("centers.name["+slotsOpenIndex.get(i)+"]").toString());
                centerAddress.add(response.path("centers.address["+slotsOpenIndex.get(i)+"]").toString());
                capacity.add(response.path("centers.sessions["+slotsOpenIndex.get(i)+"].available_capacity").toString());
                vaccineName.add(response.path("centers.sessions["+slotsOpenIndex.get(i)+"].vaccine").toString());
            }
            SendEmail.sendMail(RunAPI.printCurrentDateWithTime(),centerName,centerAddress,capacity,vaccineName);
            System.out.println("Mail Send Successfully!!!");
            slotsOpenIndex.clear();
            centerName.clear();
            centerAddress.clear();
            capacity.clear();
            vaccineName.clear();
        }

     }
}
