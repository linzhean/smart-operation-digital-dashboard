package tw.edu.ntub.imd.birc.sodd.dto.excel.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeReference implements Reference {
    private final List<Reference> referenceList;

    public CompositeReference(Reference... referenceArray) {
        this.referenceList = new ArrayList<>(Arrays.asList(referenceArray));
    }

    @Override
    public String getReference() {
        return referenceList.stream()
                .map(Reference::getReference)
                .collect(Collectors.joining(", "));
    }

    public void addReference(Reference reference) {
        referenceList.add(reference);
    }
}
