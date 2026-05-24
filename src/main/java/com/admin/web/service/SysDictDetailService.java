package com.admin.web.service;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysMove;
import com.admin.web.model.entity.SysDict;
import com.admin.web.model.entity.SysDictDetail;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.request.MoveRequest;
import com.admin.web.repository.SysDictDetailRepository;
import com.admin.web.repository.SysDictRepository;
import com.admin.web.utils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysDictDetailService {
    private final SysDictRepository sysDictRepository;
    private final SysDictDetailRepository sysDictDetailRepository;

    public SysDictDetailService(SysDictRepository sysDictRepository, SysDictDetailRepository sysDictDetailRepository) {
        this.sysDictRepository = sysDictRepository;
        this.sysDictDetailRepository = sysDictDetailRepository;
    }

    public List<SysDictDetail> all(Long id) {
        SysDict sysDict = this.sysDictRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        return sysDictDetailRepository.findByDictOrderBySort(sysDict);
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(MoveRequest request) {
        SysDictDetail sysDictDetail = this.sysDictDetailRepository.findById(request.id())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        List<SysDictDetail> sysDictDetails = this.sysDictDetailRepository.findByDictOrderBySort(sysDictDetail.getDict());
        SysMove.of(request.move(), sysDictDetail.getId(), sysDictDetails).move((index) -> {
            long sort = sysDictDetail.getSort();
            sysDictDetail.setSort(sysDictDetails.get(index).getSort());
            sysDictDetails.get(index).setSort(sort);
            this.sysDictDetailRepository.saveAll(Arrays.asList(sysDictDetail, sysDictDetails.get(index)));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SysDictDetail sysDictDetail) {
        if (Objects.nonNull(this.sysDictDetailRepository.findByLabel(sysDictDetail.getLabel()))) {
            throw new ServerResponseException("字典标签已存在！");
        }
        if (Objects.nonNull(this.sysDictDetailRepository.findByValue(sysDictDetail.getValue()))) {
            throw new ServerResponseException("字典数值已存在！");
        }
        this.sysDictDetailRepository.save(sysDictDetail);
        sysDictDetail.setSort(sysDictDetail.getId());
        this.sysDictDetailRepository.save(sysDictDetail);
    }

    public void update(SysDictDetail sysDictDetail) {
        SysDictDetail oldSysDictDetail = this.sysDictDetailRepository.findById(sysDictDetail.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(oldSysDictDetail.getLabel(), sysDictDetail.getLabel())
                && Objects.nonNull(this.sysDictDetailRepository.findByLabel(sysDictDetail.getLabel()))) {
            throw new ServerResponseException("字典标签已存在！");
        }
        if (!Objects.equals(oldSysDictDetail.getValue(), sysDictDetail.getValue())
                && Objects.nonNull(this.sysDictDetailRepository.findByValue(sysDictDetail.getValue()))) {
            throw new ServerResponseException("字典数值已存在！");
        }
        BeanUtils.copyNonNullProperties(sysDictDetail, oldSysDictDetail);
        this.sysDictDetailRepository.save(oldSysDictDetail);
    }

    public void delete(List<Long> id) {
        this.sysDictDetailRepository.deleteAllById(id);
    }
}
