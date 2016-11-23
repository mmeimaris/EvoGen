package org.imis.model;

import java.util.HashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Resource;

public class CharacteristicSet {


	
	HashSet<Resource> properties;
		
	Node node;
	
	Long longRep;
	
	public Long getLongRep() {
		return longRep;
	}

	public void setLongRep(Long longRep) {
		this.longRep = longRep;
	}

	public CharacteristicSet(HashSet<Resource> properties){
				
		this.properties = properties;		
		
	}
	
	
	
	@Override
    public int hashCode() {		
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(properties).        	
            toHashCode();
    }
	
	@Override
    public boolean equals(Object obj) {
       if (!(obj instanceof CharacteristicSet))
            return false;
        if (obj == this)
            return true;

        CharacteristicSet rhs = (CharacteristicSet) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(properties, rhs.properties).        	
            isEquals();
    }
	
	
}
