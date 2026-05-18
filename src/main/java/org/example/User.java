package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "user")
public class User {
    public String name;
    public int age;

    @JsonProperty("actual_work")
    public String actualWork;

    @JsonProperty("previous_works")
    @JacksonXmlElementWrapper(localName = "previous_works")
    @JacksonXmlProperty(localName = "work")
    public List<String> previousWorks;

    @JsonProperty("current_status_active")
    public boolean currentStatusActive;

    public User() {}

    public String getName() {
        return name;
    }

    public String getActual_work() {
        return actualWork;
    }

    public boolean isCurrent_status_active() {
        return currentStatusActive;
    }

    public List<String> getPrevious_works() {
        return previousWorks;
    }

    public int getAge() {
        return age;
    }
}