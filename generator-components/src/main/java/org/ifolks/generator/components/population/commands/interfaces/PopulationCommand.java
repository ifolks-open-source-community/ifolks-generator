package org.ifolks.generator.components.population.commands.interfaces;

import org.ifolks.generator.model.domain.business.Bean;

/**
 * Used by the populator<br>
 * Once identified the path where to find what to do, this interface just defines what to do with the given path
 * @author Nicolas Thibault
 *
 */
public interface PopulationCommand {

	public void execute(String path, Bean bean);
}
