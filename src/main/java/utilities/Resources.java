package utilities;

public class Resources {

	public static String getEndPoint= "api/user/2" ;
	public static String postEndPoint= "api/users" ;

	public Resources()
	{}

	public String boardsEndpoints(){
		String endpoint = "boards/";
		return endpoint;
	}
	public String getBoardsEndpoints(){
		String endpoint = boardsEndpoints();
		return endpoint;
	}
	public String listEndpoint(){
		String endpoint = "lists/";
		return endpoint;
	}
	public String createListEndpoint(String idBoard){
		String endpoint = boardsEndpoints()+ idBoard +"/lists";
		return endpoint;
	}
	public String getFilteredListEndpoint(String idBoard){
		String endpoint = createListEndpoint(idBoard)+"/all";
		return endpoint;
	}
	public String archiveList(String idList){
		String endpoint = "/lists/"+ idList +"/closed";
		return endpoint;
	}
	public String cardsEndpoints(String listID){
		String endpoint = listEndpoint()+listID + "/cards/";
		return endpoint;
	}
	public String membersEndpoint(String idBoard){
		String endpoint = getBoardsEndpoints()+idBoard+"/members";
		return endpoint;
	}
	public String getUserEndpoint(String user_login){
		String endpoint = "/users/" + user_login;
		return endpoint;
	}
	public String createuserSession(){
		String endpoint = "/api/session" ;
		return endpoint;
	}
	public String getMembersEndpoint(String idBoard, String idMembers){
		String endpoint = membersEndpoint(idBoard)+ "/" +idMembers;
		return endpoint;
	}
	public String OrganisationEndpoints(String idOrganization){
		String endpoint = "organization"+ "/" + idOrganization;
		return endpoint;
	}
	public String getMember(String idMember){
		String endpoint = "/members/"+ idMember;
		return endpoint;
	}
	public String newBoardBackgroud(String idMember){
		String endpoint = "/members/"+ idMember + "/boardBackgrounds";
		return endpoint;
	}
	public String createSavedSearches(String idMember){
		String endpoint = "/members/"+ idMember + "/savedSearches";
		return endpoint;
	}

	public String getCards(String idCard){
		String endpoint = "/cards/"+ idCard ;
		return endpoint;
	}
	public String createAttachment(String idcard){
		String endpoint = "/cards/"+ idcard +"/attachments";
		return endpoint;
	}
	public String deleteAttachment(String idcard, String idAttachment){
		String endpoint = "/cards/"+ idcard +"/attachments/" + idAttachment ;
		return endpoint;
	}
	public String organizationEndpoint(){
		String endpoint = "organizations" ;
		return endpoint;
	}
//	public  String archiveList= "/lists/";
//	public String getList_id() {
//		return list_id;
//	}


}
