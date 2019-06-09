package org.javatraining.uploadxmlcdcatalog.model;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "CD")
@XmlType(propOrder = {"title", "artist", "country", "company", "price", "year"})
@XmlAccessorType(XmlAccessType.FIELD)
public class MusicCompactDisk {
    @XmlElement(name="TITLE")
    private String title;

    @XmlElement(name="ARTIST")
    private String artist;

    @XmlElement(name="COUNTRY")
    private String country;

    @XmlElement(name="COMPANY")
    private String company;

    @XmlElement(name="PRICE")
    private BigDecimal price;

    @XmlElement(name="YEAR")
    private Integer year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicCompactDisk musicCompactDisk = (MusicCompactDisk) o;
        return Objects.equals(title.toLowerCase(), musicCompactDisk.title.toLowerCase()) &&
                Objects.equals(artist.toLowerCase(), musicCompactDisk.artist.toLowerCase()) &&
                Objects.equals(country.toLowerCase(), musicCompactDisk.country.toLowerCase()) &&
                Objects.equals(company.toLowerCase(), musicCompactDisk.company.toLowerCase()) &&
                Objects.equals(year, musicCompactDisk.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, country, company, year);
    }
}


