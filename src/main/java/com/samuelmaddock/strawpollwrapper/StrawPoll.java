package com.samuelmaddock.strawpollwrapper;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Samuel-Maddock on 09/08/2017.
 */

public class StrawPoll {
    private static final String API_URL = "http://strawpoll.me/api/v2/polls";
    private String id ;
    private String title;
    private List<String> options = new ArrayList<>();

    @SerializedName("multi")
    private boolean isMulti = false;

    @SerializedName("dupcheck")
    private String dupCheck = "normal";

    @SerializedName("captcha")
    private boolean hasCaptcha = false;

    private List<Integer> votes = new ArrayList<>();


    public StrawPoll() {
        this.title = "Default Poll Title";
        this.options.add("Default option 1");
        this.options.add("Default option 2");
    }

    public StrawPoll(StrawPoll poll){ //Copy Constructor
        updatePoll(poll);
    }

    public StrawPoll(String url){
        retrieve(url);
    }

    public StrawPoll(String title, String... options){
        this.title = title;
        this.options = Arrays.asList(options);
    }

    public StrawPoll(String title, List<String> options){
        this.title = title;
        this.options = options;
    }

    public StrawPoll(String title, List<String> options, boolean isMulti, boolean hasCaptcha, DupCheckType dupCheck){
       this(title, options);
       this.isMulti = isMulti;
       this.hasCaptcha = hasCaptcha;
       this.dupCheck = dupCheck.name();
    }

    private HttpURLConnection createConnection(String apiUrl, String request) throws IOException{
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(request);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "StrawPoll Java API Wrapper 1.0.0");
        connection.setDoInput(true);

        if(request.equals("POST")){
            connection.setDoOutput(true);
        }

        return connection;
    }

    public void create() {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(this);

        try{
            HttpURLConnection connection = createConnection(API_URL, "POST");
            connection.setRequestProperty("Content-Length", Integer.toString(jsonMessage.length()));

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(jsonMessage);
            wr.flush();
            wr.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String returnedJSON = br.readLine();
            StrawPoll returnedPoll = gson.fromJson(returnedJSON, StrawPoll.class);
            updatePoll(returnedPoll);
        } catch(IOException e){
            e.printStackTrace();;
        }
    }

    public void update() {
        try{
            HttpURLConnection connection = createConnection(API_URL + "/" + this.id, "GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonMessage = br.readLine();
            Gson gson = new Gson();
            StrawPoll returnedPoll = gson.fromJson(jsonMessage, StrawPoll.class);
            updatePoll(returnedPoll);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void updatePoll(StrawPoll poll){
        this.id = poll.getId();
        this.title = poll.getTitle();
        this.options = poll.getOptions();
        this.isMulti = poll.isMulti();
        this.dupCheck = poll.getDupCheck().name();
        this.hasCaptcha = poll.hasCaptcha();
        this.votes = poll.getVotes();
    }

    public void retrieve(String url){
        //TODO
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public StrawPoll setTitle(String title) {
        this.title = title;
        return this;
    }

    public void setOptions(List<String> options){
        this.options = options;
    }

    public void addOptions(String... option){
        this.options.addAll(Arrays.asList(option));
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getVotes() {
        return votes;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public StrawPoll setisMulti(boolean multi) {
        isMulti = multi;
        return this;
    }

    public boolean hasCaptcha() {
        return hasCaptcha;
    }

    public StrawPoll setHasCaptcha(boolean hasCaptcha) {
        this.hasCaptcha = hasCaptcha;
        return this;
    }

    public DupCheckType getDupCheck() {
        return DupCheckType.valueOf(dupCheck.toUpperCase());
    }

    public StrawPoll setDupCheck(DupCheckType dupCheck) {
        this.dupCheck = dupCheck.name();
        return this;
    }
}
