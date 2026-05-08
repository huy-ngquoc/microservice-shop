package vn.uit.edu.msshop.image.application.port.in;

import java.io.IOException;

import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.dto.command.RollbackImageFolderCommand;


public interface RemoveImageFolderUseCase {
    public void removeImageFolder(RemoveImageFolderCommand command) throws IOException;
    public void rollbackImageFolder(RollbackImageFolderCommand command) throws IOException;
}
