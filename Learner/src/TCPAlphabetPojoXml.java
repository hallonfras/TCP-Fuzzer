import com.github.protocolfuzzing.protocolstatefuzzer.components.learner.alphabet.xml.AlphabetPojoXml;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "alphabet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TCPAlphabetPojoXml extends AlphabetPojoXml<TCPInput> {

    @XmlElements(value = {
            @XmlElement(type = TCPInputPojoXml.class, name = "TCPInput")
    })
    private List<TCPInputPojoXml> xmlInputs;

    public TCPAlphabetPojoXml() {
        xmlInputs = new ArrayList<>();
    }

    public List<TCPInput> getInputs() {
        List<TCPInput> allInputs = xmlInputs.stream().map(xmlInput -> new TCPInput(xmlInput.name))
                .collect(Collectors.toList());
        return allInputs;
    }

    public static class TCPInputPojoXml {
        @XmlAttribute(name = "name", required = true)
        private String name;
    }
}
