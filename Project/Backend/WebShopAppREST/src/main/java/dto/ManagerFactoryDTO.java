package dto;

public class ManagerFactoryDTO {
	
	private int factoryId;
	private String managerUsername;
	public ManagerFactoryDTO(int factoryId, String managerUsername) {
		super();
		this.factoryId = factoryId;
		this.managerUsername = managerUsername;
	}
	public ManagerFactoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public String getManagerUsername() {
		return managerUsername;
	}
	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	} 
	
	

}
