package dk.kb.license.validation;

import dk.kb.util.webservice.exception.InvalidArgumentServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InputValidatorTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "ds.tv:oai:io:",
            "ds.radio:oai:io:",
            "ds.tv:oai:io:0bdf8656-4a96-400d-b3d8-e4695328688",
            "ds.tv:oai:io:0bdf8656-4a96-400d-b3d8-e4695328688e"
    })
    public void validateDsId_whenValidDsId_thenDoNotThrow(String dsId) {
        // Arrange
        InputValidator inputValidator = mock(InputValidator.class);
        doCallRealMethod().when(inputValidator).validateDsId(dsId);

        // Act and assert
        // validateDsId() has return type void, so we can only check that it did not throw exception
        assertDoesNotThrow(() -> inputValidator.validateDsId(dsId));

        // and it only was called once
        verify(inputValidator, times(1)).validateDsId(dsId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "1",
            "0bdf8656-4a96-400d-b3d8-e4695328688e",
            ":0bdf8656-4a96-400d-b3d8-e4695328688e",
            "ds.tv:oai:io0bdf8656-4a96-400d-b3d8-e4695328688e",
            "\"ds.tv:oai:io:0bdf8656-4a96-400d-b3d8-e4695328688e\""
    })
    public void validateDsId_whenInvalidDsId_thenThrowInvalidArgumentServiceException(String dsId) {
        // Arrange
        String expectedMessage = "Invalid dsId: " + dsId;
        InputValidator inputValidator = new InputValidator();

        // Act
        Exception exception = assertThrows(InvalidArgumentServiceException.class, () -> inputValidator.validateDsId(dsId));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void validateDsId_whenNull_thenThrowInvalidArgumentServiceException() {
        // Arrange
        String expectedMessage = "Invalid dsId: " + null;
        InputValidator inputValidator = new InputValidator();

        // Act
        Exception exception = assertThrows(InvalidArgumentServiceException.class, () -> inputValidator.validateDsId(null));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void validateCommentLength_whenCommentIsNull_thenDoNotThrow() {
        // Arrange
        InputValidator inputValidator = mock(InputValidator.class);
        doCallRealMethod().when(inputValidator).validateCommentLength(null);

        // Act and assert
        // validateCommentLength() has return type void, so we can only check that it did not throw exception
        assertDoesNotThrow(() -> inputValidator.validateCommentLength(null));

        // and it only was called once
        verify(inputValidator, times(1)).validateCommentLength(null);
    }

    @Test
    public void validateCommentLength_whenEmpty_thenDoNotThrow() {
        // Arrange
        String comment = "";
        InputValidator inputValidator = mock(InputValidator.class);
        doCallRealMethod().when(inputValidator).validateCommentLength(comment);

        // Act and assert
        // validateCommentLength() has return type void, so we can only check that it did not throw exception
        assertDoesNotThrow(() -> inputValidator.validateCommentLength(comment));

        // and it only was called once
        verify(inputValidator, times(1)).validateCommentLength(comment);
    }

    @Test
    public void validateCommentLength_whenValidComment_thenDoNotThrow() {
        // Arrange
        String comment = "Here is some text";
        InputValidator inputValidator = mock(InputValidator.class);
        doCallRealMethod().when(inputValidator).validateCommentLength(comment);

        // Act and assert
        // validateCommentLength() has return type void, so we can only check that it did not throw exception
        assertDoesNotThrow(() -> inputValidator.validateCommentLength(comment));

        // and it only was called once
        verify(inputValidator, times(1)).validateCommentLength(comment);
    }

    @Test
    public void validateCommentLength_whenTooLongComment_thenThrowInvalidArgumentServiceException() {
        // Arrange
        // Use String repeat() method to create a long comment
        String longComment = "x".repeat(16349);
        String expectedMessage = "Comment was too long and cannot be added to rights module. Only 16348 characters are allowed.";
        InputValidator inputValidator = new InputValidator();

        // Act
        Exception exception = assertThrows(InvalidArgumentServiceException.class, () -> inputValidator.validateCommentLength(longComment));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void validateDrProductionIdFormat_whenValidDrProductionId_thenDoNotThrow() {
        // Arrange
        String  productionId = "1234567890";
        InputValidator inputValidator = mock(InputValidator.class);
        doCallRealMethod().when(inputValidator).validateDrProductionIdFormat(productionId);

        // Act and assert
        // validateDrProductionIdFormat() has return type void, so we can only check that it did not throw exception
        assertDoesNotThrow(() -> inputValidator.validateDrProductionIdFormat(productionId));

        // and it only was called once
        verify(inputValidator, times(1)).validateDrProductionIdFormat(productionId);
    }

    @Test
    public void validateDrProductionIdFormat_whenEmptyDrProductionId_thenThrowInvalidArgumentServiceException() {
        // Arrange
        String drProductionId = "";
        String expectedMessage = "The input DR production ID:  should only contain digits";
        InputValidator inputValidator = new InputValidator();

        // Act
        Exception exception = assertThrows(InvalidArgumentServiceException.class, () -> inputValidator.validateDrProductionIdFormat(drProductionId));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void validateDrProductionIdFormat_whenTooShortDrProductionId_thenThrowInvalidArgumentServiceException() {
        // Arrange
        String drProductionId = "12345";
        String expectedMessage = "The input DR production ID: 12345 should be at least 8 digits";
        InputValidator inputValidator = new InputValidator();

        // Act
        Exception exception = assertThrows(InvalidArgumentServiceException.class, () -> inputValidator.validateDrProductionIdFormat(drProductionId));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void validateDrProductionIdFormat_whenInvalidDrProductionId_thenThrowInvalidArgumentServiceException() {
        // Arrange
        String productionId = "12345abcde";
        String expectedMessage = "The input DR production ID: 12345abcde should only contain digits";
        InputValidator inputValidator = new InputValidator();

        // Act
        Exception exception = assertThrows(InvalidArgumentServiceException.class, () -> inputValidator.validateDrProductionIdFormat(productionId));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}
