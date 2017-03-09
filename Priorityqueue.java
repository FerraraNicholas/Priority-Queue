package csi403;


// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
import java.util.List;
import java.util.Arrays;
import java.util.*;
import java.lang.*;


// Extend HttpServlet class
public class ReverseList extends HttpServlet {

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      doService(request, response); 
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }


  // Our main worker method
  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
  // Returns the list reversed.   
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);

      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");
      // Reverse the data in the list
      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();

      //Set response content type to be JSON
      response.setContentType("application/json");
      //Send back the response JSON message
      PrintWriter out = response.getWriter();
      //Initaliaze the JSON array
      JsonObject home = inArray.getJsonObject(0);
      //Initaliaze the linked list with type JSON object
      LinkedList<JsonObject> list = new LinkedList<JsonObject>();

      try{
       //Iterate over the data of the inList
      for (int i = 0; i < inArray.size(); i++){
        //Capture each object in the JSON Object variable
        home = inArray.getJsonObject(i);
        //If the value of cmd is equal to enqueue
        if(home.getString("cmd").equals("enqueue")){
          //Then add it to our linked list
          list.add(home);
        }else{ //Otherwise if you get a dequeue
          //Iterate over the data the the linked list -1
          for (int j = 0; j < list.size()-1; j++){
            //If the value of prioirty is greater than the value of the next
            if(list.get(j).getInt("pri") > list.get(j+1).getInt("pri")){
              //remove it
              list.remove(j+1);
            }

          }

        }

      }

}   //Catch the exceptions of bad JSON data
    catch(Exception e){
      out.print("{ ");
      out.print("\"message\" : ");
      out.print("\"Malformed JSON\"");
      out.print(" }");
      return;
    }

      //Iterate over the list in reverse and add it to outArrayBuilder
      for (int i = list.size() - 1; i >= 0; i--) {
        outArrayBuilder.add(list.get(i)); 
      }

      //Print the contents of outArrayBuilder in JSON format
      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}");

  }
  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}

