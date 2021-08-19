package com.demo.graphql.demographql.restful;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({BasicInfo.class, Default.class})
public interface ValidGroupSequence {
}
