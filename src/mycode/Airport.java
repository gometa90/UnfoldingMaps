package mycode;

public class Airport implements Comparable<Airport> {

	private String city;
	private String country;
	private String airportCode;

	public Airport(String city, String country, String airportCode) {
		super();
		this.city = city;
		this.country = country;
		this.airportCode = airportCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	@Override
	public int compareTo(Airport other) {
		// I want to compare for sorting by country
		return (this.getCountry().compareTo(other.getCountry()));
	}
}
