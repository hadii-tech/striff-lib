package com.hadii.striff.diagram;

import com.google.common.collect.Sets;
import com.hadii.striff.ChangeSet;
import com.hadii.striff.extractor.ComponentRelation;
import com.hadii.striff.metrics.OOPMetricsChangeAnalyzer;
import com.hadii.striff.parse.CodeDiff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the set of components and relations that are to be displayed in a
 * Striff diagram.
 */
public class StriffDiagramModel {

    private final Set<DiagramComponent> diagramCmps = new HashSet<>();
    private final Set<ComponentRelation> contextRelations = new HashSet<>();
    private Set<ComponentRelation> coreRelations = new HashSet<>();
    private static final Logger LOGGER = LogManager.getLogger(StriffDiagramModel.class);

    public StriffDiagramModel(CodeDiff codeDiff) {
        this(codeDiff, Collections.emptySet());
    }

    public StriffDiagramModel(CodeDiff codeDiff, Set<String> sourceFilesFilter) {
        LOGGER.info("Generating diagram model..");
        OOPMetricsChangeAnalyzer oopMetricsChangeAnalyzer = new OOPMetricsChangeAnalyzer(
                codeDiff.oldModel(), codeDiff.newModel(), sourceFilesFilter);
        calculateCoreBaseCmps(codeDiff, sourceFilesFilter).forEach(
                cmpName -> this.diagramCmps.add(new DiagramComponent(
                        cmpName, oopMetricsChangeAnalyzer.analyzeChanges(cmpName).get(), codeDiff.mergedModel())));
        calculateCoreRels(codeDiff.changeSet());
    }

    private Set<String> calculateCoreBaseCmps(CodeDiff codeDiff, Set<String> sourceFilesFilter) {
        Set<String> diagramCmpNames = new HashSet<>();
        ChangeSet changeSet = codeDiff.changeSet();
        Set<String> unfilteredCoreCmps = Stream.of(changeSet.addedComponents(),
                changeSet.deletedComponents(),
                changeSet.keyRelationsComponents(),
                changeSet.modifiedComponents()).flatMap(Collection::stream).collect(Collectors.toSet());
        if (!sourceFilesFilter.isEmpty()) {
            unfilteredCoreCmps = unfilteredCoreCmps.stream().filter(
                    cmp -> sourceFilesFilter.contains(codeDiff.mergedModel().getComponent(cmp).get().sourceFile()))
                    .collect(Collectors.toSet());
        }
        unfilteredCoreCmps.forEach(diagramComponent -> {
            if (codeDiff.mergedModel().getComponent(diagramComponent).get().componentType().isBaseComponent()) {
                diagramCmpNames.add(diagramComponent);
            } else {
                DiagramComponent parentComponent = new DiagramComponent(
                        codeDiff.mergedModel().parentBaseCmp(diagramComponent), codeDiff.mergedModel());
                if (parentComponent != null) {
                    diagramCmpNames.add(parentComponent.uniqueName());
                }
            }
        });
        LOGGER.info(this.diagramCmps.size() + " components will be displayed.");
        return diagramCmpNames;
    }

    private void calculateCoreRels(ChangeSet changeSet) {
        this.coreRelations = Stream.of(changeSet.addedRelations().allRels(),
                changeSet.deletedRelations().allRels()).flatMap(Collection::stream).collect(Collectors.toSet());
        LOGGER.info(this.coreRelations.size() + " relations will be displayed.");
    }

    public Set<DiagramComponent> allBaseCmps() {
        return this.diagramCmps.stream().filter(cmp -> cmp.componentType().isBaseComponent())
                .collect(Collectors.toSet());
    }

    public Set<DiagramComponent> diagramCmps() {
        return this.diagramCmps;
    }

    public Set<ComponentRelation> coreRels() {
        return this.coreRelations;
    }

    public Set<ComponentRelation> contextRels() {
        return this.contextRelations;
    }

    public Set<ComponentRelation> allRels() {
        return Sets.union(this.contextRelations, this.coreRelations);
    }

    public boolean empty() {
        return this.diagramCmps.isEmpty();
    }
}
