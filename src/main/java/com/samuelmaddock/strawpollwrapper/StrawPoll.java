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
    private static transient final String API_URL = "http://strawpoll.me/api/v2/polls";
    private String id = "";
    private String title;
    private List<String> options = new ArrayList<>();

    @SerializedName("multi")
    private boolean isMulti = false;

    @SerializedName("dupcheck")
    private String dupCheck = "normal";

    @SerializedName("captcha")
    private boolean hasCaptcha = false;

    private List<Integer> votes = new ArrayList<>();
    private transient String pollURL = "";

    public StrawPoll() {
        this.title = "Default Poll Title";
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

    /**
     * This creates a strawpoll by sending a POST request to the strawpoll API.
     * The poll will be created with the current title, options and boolean settings.
     * <p>After calling this, the {@code pollURL} will be updated to the url of the created poll.</p>
     */
    public void create() {
        if (this.options.size() == 0){
            throw new RuntimeException("Cannot create a poll with no options.");
        }

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

    /**
     * This retrieves the strawpolls information (using the current id) from the API via a GET request.
     * It then updates the current StrawPoll object to reflect any changes in the poll.
     */
    public void update() {

        if (this.id.equals("")){
            return;
        }

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
        this.pollURL = API_URL + "/" + poll.getId();
    }

    /**
     * This retrieves the strawpolls information from the url given.
     * It then returns a StrawPoll object with the data retrieved.
     * @param url - The url of the strawpoll
     * @return - The strawpoll object
     */
    public StrawPoll retrieve(String url){
        try{
            HttpURLConnection connection = createConnection(url, "GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonMessage = br.readLine();
            Gson gson = new Gson();
            StrawPoll returnedPoll = gson.fromJson(jsonMessage, StrawPoll.class);
            return returnedPoll;
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This gets the id of the created strawpoll object.
     * If the strawpoll has not yet been created via {@code create()} then the id will be empty by default.
     * @return - The string id of the strawpoll
     */
    public String getId() {
        return id;
    }

    /**
     * This gets the title of the strawpoll object.
     * If the title has not yet been set, then by default the title is "Default Poll Title"
     * @return - The string title of the poll.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This will set the value of the strawpoll object title.
     * @param title - The title of the strawpoll.
     * @return - The instance of this class (for method-chaining)
     */
    public StrawPoll setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * This will set the options of strawpoll object.
     * Note that this will clear any previously stored options.
     * If you want to add options us {@code addOptions()} instead.
     * @param options - The {@code List<String>} collection of options
     */
    public void setOptions(List<String> options){
        this.options = options;
    }

    /**
     * This adds options to the strawpoll object.
     * @param options - A list of string options to be added (can be one or multiple)
     */
    public void addOptions(String... options){
        this.options.addAll(Arrays.asList(options));
    }

    /**
     *This gets the list of options stored in the strawpoll object
     * @return - {@code List<String>} of options
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * This gets a list of votes
     * @return - {@code List<Integer>} of votes
     */
    public List<Integer> getVotes() {
        return votes;
    }

    /**
     * This gets the boolean of isMulti()
     * If this is true, then the poll will allow multiple votes from a single user
     * If this is false, then the poll will not allow multiple votes from the same user.
     * @return
     */
    public boolean isMulti() {
        return isMulti;
    }

    /**
     * This sets the boolean of isMulti()
     * <p>If this is true, then the poll will allow multiple votes from a single </p>
     * <p>If this is false, then the poll will not allow multiple votes from the same user.</p>
     * @param multi - The boolean to set
     * @return - The instance of this class (for method-chaining)
     */
    public StrawPoll setisMulti(boolean multi) {
        isMulti = multi;
        return this;
    }

    /**
     * This checks to see if the poll has a captcha.
     * <p>If this is true then the poll will have a captcha to verify they are not a bot</p>
     * <p> If this is false, the poll will not have a captcha</p>
     * @return - The boolean that determines if the poll has a captcha
     */
    public boolean hasCaptcha() {
        return hasCaptcha;
    }

    /**
     *
     * @param hasCaptcha - The boolean to determine if the poll has a captcha
     * @return - The instance of this class (for method-chaining)
     */
    public StrawPoll setHasCaptcha(boolean hasCaptcha) {
        this.hasCaptcha = hasCaptcha;
        return this;
    }

    /**
     * This retrieves the type of duplication checking that the poll is using.
     * This returns an enum called DupCheckType that can take one of three values
     *      <p> DupCheckType.NORMAL - Normal duplication checking is enabled (default value) </p>
     *      <p> DupCheckType.PERMISSIVE - The poll is more lenient in vote duplication checking.</p>
     *      <p> DupCheckType.DISABLED - The poll will not check for duplication </p>
     * @return - The enum DupCheckType that determines the type of duplication checking.
     */
    public DupCheckType getDupCheck() {
        return DupCheckType.valueOf(dupCheck.toUpperCase());
    }

    /**
     *This sets the type of duplication checking that the poll is using.
     *      <p> DupCheckType.NORMAL - Normal duplication checking is enabled (default value) </p>
     *      <p> DupCheckType.PERMISSIVE - The poll is more lenient in vote duplication checking.</p>
     *      <p> DupCheckType.DISABLED - The poll will not check for duplication </p>
     * @param dupCheck - The duplication checking type to be used by the poll
     * @return - The instance of this class (for method-chaining)
     */
    public StrawPoll setDupCheck(DupCheckType dupCheck) {
        this.dupCheck = dupCheck.name();
        return this;
    }

    /**
     * This gets the full url of a created strawpoll.
     * @return - The full url of the created strawpoll.
     */
    public String getPollURL(){
        return this.pollURL;
    }

    /**
     * This converts the current strawpoll object into JSON
     * @return - The raw JSON of the strawpoll object
     */
    public String toRawJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
