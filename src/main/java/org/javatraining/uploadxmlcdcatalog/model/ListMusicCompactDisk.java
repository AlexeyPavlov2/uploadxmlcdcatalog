package org.javatraining.uploadxmlcdcatalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "CATALOG")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListMusicCompactDisk {

    @XmlElement(name = "CD")
    private List<MusicCompactDisk> disks = null;
}
