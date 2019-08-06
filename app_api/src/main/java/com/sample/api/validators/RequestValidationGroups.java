package com.sample.api.validators;

import javax.validation.groups.Default;

public interface RequestValidationGroups {

    interface Get extends Default {
    }

    interface Create extends Default {
    }

    interface Update extends Default {
    }

    interface Delete extends Default {
    }
}
