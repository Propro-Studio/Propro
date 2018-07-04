package com.westlake.air.swathplatform.algorithm;

import com.westlake.air.swathplatform.constants.ResidueType;
import com.westlake.air.swathplatform.dao.AminoAcidDAO;
import com.westlake.air.swathplatform.dao.ElementsDAO;
import com.westlake.air.swathplatform.domain.bean.Fragment;
import com.westlake.air.swathplatform.domain.db.TransitionDO;
import com.westlake.air.swathplatform.parser.UnimodParser;
import com.westlake.air.swathplatform.parser.model.chemistry.Unimod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-06-19 13:39
 */
@Component
public class FormulaCalculator {

    public final Logger logger = LoggerFactory.getLogger(FormulaCalculator.class);

    @Autowired
    AminoAcidDAO aminoAcidDAO;

    @Autowired
    ElementsDAO elementsDAO;

    @Autowired
    UnimodParser unimodParser;

    public double getMonoMz(TransitionDO transitionDO) {
        return getMonoMz(transitionDO.getSequence(), ResidueType.Full, transitionDO.getPrecursorCharge(), 0, 0, false, parseUnimodIds(transitionDO));
    }

    public double getAverageMz(TransitionDO transitionDO) {
        return getAverageMz(transitionDO.getSequence(), ResidueType.Full, transitionDO.getPrecursorCharge(), 0, 0, false, parseUnimodIds(transitionDO));
    }

    /**
     * 本函数会考虑Modification的情况
     *
     * @param fragment
     * @return
     */
    public double getMonoMz(Fragment fragment) {

        return getMonoMz(fragment.getSequence(), fragment.getType(), fragment.getCharge(), fragment.getAdjust(), fragment.getDeviation(), fragment.isIsotope(), parseUnimodIds(fragment));
    }

    /**
     * 本函数会考虑Modification的情况
     *
     * @param fragment
     * @return
     */
    public double getAverageMz(Fragment fragment) {

        return getAverageMz(fragment.getSequence(), fragment.getType(), fragment.getCharge(), fragment.getAdjust(), fragment.getDeviation(), fragment.isIsotope(), parseUnimodIds(fragment));
    }

    public double getMonoMz(String sequence, String type, int charge, int adjust, double deviation, boolean isIsotope, List<String> unimodIds) {
        double unimodMonoMass = 0;
        if (unimodIds != null) {
            for (String unimodId : unimodIds) {
                Unimod unimod = unimodParser.getUnimod(unimodId);
                if (unimod != null) {
                    unimodMonoMass += unimod.getMonoMass();
                }
            }
        }

        switch (type) {
            case ResidueType.Full:
                return (getMonoWeightAsFull(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.AIon:
                return (getMonoWeightAsAIon(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.BIon:
                return (getMonoWeightAsBIon(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.CIon:
                return (getMonoWeightAsCIon(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.XIon:
                return (getMonoWeightAsXIon(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.YIon:
                return (getMonoWeightAsYIon(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.ZIon:
                return (getMonoWeightAsZIon(sequence) + getMonoHWeight(charge) + adjust + unimodMonoMass + (isIsotope ? 1 : 0)) / charge + deviation;
            default:
                return 0;
        }
    }

    public double getAverageMz(String sequence, String type, int charge, int adjust, double deviation, boolean isIsotope, List<String> unimodIds) {

        double unimodAverageMass = 0;
        if (unimodIds != null) {
            for (String unimodId : unimodIds) {
                Unimod unimod = unimodParser.getUnimod(unimodId);
                if (unimod != null) {
                    unimodAverageMass += unimod.getAverageMass();
                }
            }
        }

        switch (type) {
            case ResidueType.Full:
                return (getAverageWeightAsFull(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.AIon:
                return (getAverageWeightAsAIon(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.BIon:
                return (getAverageWeightAsBIon(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.CIon:
                return (getAverageWeightAsCIon(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.XIon:
                return (getAverageWeightAsXIon(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.YIon:
                return (getAverageWeightAsYIon(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            case ResidueType.ZIon:
                return (getAverageWeightAsZIon(sequence) + getAverageHWeight(charge) + adjust + unimodAverageMass + (isIsotope ? 1 : 0)) / charge + deviation;
            default:
                return 0;
        }
    }

    private double getMonoHWeight(int charge) {
        return elementsDAO.getMonoWeight("H:" + charge);
    }

    private double getAverageHWeight(int charge) {
        return elementsDAO.getAverageWeight("H:" + charge);
    }

    private double getAcidMonoWeight(String sequence) {
        double monoWeight = 0;
        for (char acidCode : sequence.toCharArray()) {
            monoWeight += aminoAcidDAO.getAminoAcidByCode(String.valueOf(acidCode)).getMonoIsotopicMass();
        }
        return monoWeight;
    }

    private double getMonoWeightAsFull(String sequence) {
        return getAcidMonoWeight(sequence) + elementsDAO.getMonoWeight("H:2,O:1");
    }

    private double getMonoWeightAsNTerm(String sequence) {
        return getAcidMonoWeight(sequence) + elementsDAO.getMonoWeight("H:1");
    }

    private double getMonoWeightAsCTerm(String sequence) {
        return getAcidMonoWeight(sequence) + elementsDAO.getMonoWeight("O:1,H:1");
    }

    private double getMonoWeightAsAIon(String sequence) {
        return getMonoWeightAsNTerm(sequence) - elementsDAO.getMonoWeight("C:1,H:1,O:1");
    }

    private double getMonoWeightAsBIon(String sequence) {
        return getMonoWeightAsNTerm(sequence) - elementsDAO.getMonoWeight("H:1");
    }

    private double getMonoWeightAsCIon(String sequence) {
        return getMonoWeightAsNTerm(sequence) - elementsDAO.getMonoWeight("N:1,H:2");
    }

    private double getMonoWeightAsXIon(String sequence) {
        return getMonoWeightAsCTerm(sequence) + elementsDAO.getMonoWeight("C:1,O:1") - elementsDAO.getMonoWeight("H:1");
    }

    private double getMonoWeightAsYIon(String sequence) {
        return getMonoWeightAsCTerm(sequence) + elementsDAO.getMonoWeight("H:1");
    }

    private double getMonoWeightAsZIon(String sequence) {
        return getMonoWeightAsCTerm(sequence) + elementsDAO.getMonoWeight("N:1,H:2");
    }

    /**
     * 获取平均分子质量
     *
     * @return
     */
    private double getAcidAverageWeight(String sequence) {
        double averageWeight = 0;
        for (char acidCode : sequence.toCharArray()) {
            averageWeight += aminoAcidDAO.getAminoAcidByCode(String.valueOf(acidCode)).getAverageMass();
        }
        return averageWeight;
    }

    private double getAverageWeightAsFull(String sequence) {
        return getAcidAverageWeight(sequence) + elementsDAO.getAverageWeight("H:2,O:1");
    }

    private double getAverageWeightAsNTerm(String sequence) {
        return getAcidAverageWeight(sequence) + elementsDAO.getAverageWeight("H:1");
    }

    private double getAverageWeightAsCTerm(String sequence) {
        return getAcidAverageWeight(sequence) + elementsDAO.getAverageWeight("O:1,H:1");
    }

    private double getAverageWeightAsAIon(String sequence) {
        return getAverageWeightAsNTerm(sequence) - elementsDAO.getAverageWeight("C:1,H:1,O:1");
    }

    private double getAverageWeightAsBIon(String sequence) {
        return getAverageWeightAsNTerm(sequence) - elementsDAO.getAverageWeight("H:1");
    }

    private double getAverageWeightAsCIon(String sequence) {
        return getAverageWeightAsNTerm(sequence) - elementsDAO.getAverageWeight("N:1,H:2");
    }

    private double getAverageWeightAsXIon(String sequence) {
        return getAverageWeightAsCTerm(sequence) + elementsDAO.getAverageWeight("C:1,O:1") - elementsDAO.getAverageWeight("H:1");
    }

    private double getAverageWeightAsYIon(String sequence) {
        return getAverageWeightAsCTerm(sequence) + elementsDAO.getAverageWeight("H:1");
    }

    private double getAverageWeightAsZIon(String sequence) {
        return getAverageWeightAsCTerm(sequence) + elementsDAO.getAverageWeight("N:1,H:2");
    }

    private List<String> parseUnimodIds(Fragment fragment) {
        List<String> unimodIds = null;
        HashMap<Integer, String> map = fragment.getUnimodMap();
        if (map != null) {
            unimodIds = new ArrayList<>();
            for (Integer key : map.keySet()) {
                if (key >= fragment.getStart() && key <= fragment.getEnd()) {
                    unimodIds.add(map.get(key));
                }
            }
        }
        return unimodIds;
    }

    private List<String> parseUnimodIds(TransitionDO transitionDO) {
        List<String> unimodIds = null;
        HashMap<Integer, String> map = transitionDO.getUnimodMap();
        if (map != null) {
            unimodIds = new ArrayList<>();
            for (Integer key : map.keySet()) {
                unimodIds.add(map.get(key));
            }
        }
        return unimodIds;
    }

}
